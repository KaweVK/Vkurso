package com.kawevk.vkurso.lesson;

import com.kawevk.vkurso.enrollment.EnrollmentService;
import com.kawevk.vkurso.lesson.dtos.CreateLessonRequest;
import com.kawevk.vkurso.lesson.dtos.LessonResponse;
import com.kawevk.vkurso.lesson.dtos.UpdateLessonRequest;
import com.kawevk.vkurso.lesson.dtos.VideoUrlResponse;
import com.kawevk.vkurso.lesson.exceptions.LessonNotFoundException;
import com.kawevk.vkurso.lesson.exceptions.LessonWithoutVideoException;
import com.kawevk.vkurso.lesson.exceptions.VideoAccessDeniedException;
import com.kawevk.vkurso.module.Module;
import com.kawevk.vkurso.module.ModuleRepository;
import com.kawevk.vkurso.module.exceptions.ModuleNotFoundException;
import com.kawevk.vkurso.shared.storage.VideoStorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LessonService {

    private final LessonRepository repository;
    private final VideoStorageService storage;
    private final ModuleRepository moduleRepository;
    private final EnrollmentService enrollmentService;

    public LessonService(LessonRepository repository, VideoStorageService storage, ModuleRepository moduleRepository, EnrollmentService enrollmentService) {
        this.repository = repository;
        this.storage = storage;
        this.moduleRepository = moduleRepository;
        this.enrollmentService = enrollmentService;
    }

    @Transactional(readOnly = true)
    public Page<LessonResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(LessonResponse::from);
    }

    @Transactional(readOnly = true)
    public LessonResponse findById(Long id) {
        return LessonResponse.from(getLessonOrThrow(id));
    }

    @Transactional
    public LessonResponse create(Long moduleId, CreateLessonRequest request) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ModuleNotFoundException(moduleId));

        Lesson lesson = new Lesson(
                request.title(),
                request.description(),
                request.orderIndex()
        );

        module.addLesson(lesson);

        return LessonResponse.from(repository.save(lesson));
    }

    @Transactional
    public LessonResponse update(Long id, UpdateLessonRequest request) {
        Lesson lesson = getLessonOrThrow(id);

        lesson.setTitle(request.title());
        lesson.setDescription(request.description());
        lesson.setOrderIndex(request.orderIndex());

        return LessonResponse.from(lesson);
    }

    @Transactional
    public void delete(Long id) {
        Lesson lesson = getLessonOrThrow(id);

        lesson.getModule().removeLesson(lesson);

        if (lesson.getVideoKey() != null) {
            storage.delete(lesson.getVideoKey());
        }
        repository.delete(lesson);
    }

    @Transactional
    public LessonResponse attachVideo(Long lessonId, MultipartFile file) {
        Lesson lesson = getLessonOrThrow(lessonId);
        Long courseId = lesson.getModule().getCourse().getId();

        if (lesson.getVideoKey() != null) {
            storage.delete(lesson.getVideoKey());
        }

        String key = storage.upload(courseId, lessonId, file);
        lesson.setVideoKey(key);
        // duration: extrair do arquivo aqui (ex. ffprobe / lib de metadata)
        return LessonResponse.from(lesson, storage);
    }

    @Transactional(readOnly = true)
    public VideoUrlResponse videoUrl(Long lessonId, Long studentId) {
        Lesson lesson = getLessonOrThrow(lessonId);

        if (lesson.getVideoKey() == null) {
            throw new LessonWithoutVideoException();
        }

        Long courseId = lesson.getModule().getCourse().getId();
        boolean liberado = lesson.isFreePreview() || enrollmentService.isEnrolled(studentId, courseId);
        if (!liberado) {
            throw new VideoAccessDeniedException(lessonId);
        }

        return new VideoUrlResponse(storage.presignedGetUrl(lesson.getVideoKey()));
    }

    private Lesson getLessonOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new LessonNotFoundException(id));
    }
}
