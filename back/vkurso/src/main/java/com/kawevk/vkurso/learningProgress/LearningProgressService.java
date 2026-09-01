package com.kawevk.vkurso.learningProgress;

import com.kawevk.vkurso.course.Course;
import com.kawevk.vkurso.course.CourseRepository;
import com.kawevk.vkurso.course.exceptions.CourseNotFoundException;
import com.kawevk.vkurso.learningProgress.dtos.CourseProgressResponse;
import com.kawevk.vkurso.learningProgress.dtos.CreateLearningProgressRequest;
import com.kawevk.vkurso.learningProgress.dtos.LearningProgressResponse;
import com.kawevk.vkurso.learningProgress.dtos.UpdateLearningProgressRequest;
import com.kawevk.vkurso.learningProgress.exceptions.ProgressNotFoundException;
import com.kawevk.vkurso.lesson.LessonRepository;
import com.kawevk.vkurso.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class LearningProgressService {

    private final LearningProgressRepository repository;
    private final LearningProgressMapper mapper;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    public LearningProgressService(LearningProgressRepository repository, LearningProgressMapper mapper, CourseRepository courseRepository, LessonRepository lessonRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
    }

    @Transactional(readOnly = true)
    public Page<LearningProgressResponse> findAll(Pageable pageable, User user) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public LearningProgressResponse findByLesson(Long lessonId) {
        return mapper.toResponse(getProgressOrElseThrow(lessonId));
    }

    @Transactional(readOnly = true)
    public Page<CourseProgressResponse> getStudentCourses(@AuthenticationPrincipal User user, Pageable pageable) {
        Long studentId = user.getId();
        Page<Object[]> progressData = repository.countCompletedLessonsByStudent(studentId, pageable);

        return progressData.map(data -> {
            Long courseId = (Long) data[0];
            Long completedLessons = ((Number) data[1]).longValue();

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new CourseNotFoundException("Curso não encontrado"));

            Long totalLessons = lessonRepository.countByCourseId(courseId);

            int percentage = calculatePercentage(completedLessons, totalLessons);

            CourseProgressResponse.Status status =
                    percentage >= 100
                            ? CourseProgressResponse.Status.COMPLETED
                            : CourseProgressResponse.Status.IN_PROGRESS;

            return new CourseProgressResponse(
                    course.getId(),
                    course.getTitle(),
                    course.getSlug(),
                    course.getDescription(),
                    completedLessons,
                    totalLessons,
                    percentage,
                    status
            );
        });
    }

    @Transactional
    public LearningProgressResponse create(CreateLearningProgressRequest request, User user) {
        LearningProgress progress = new LearningProgress(
                user.getId(),
                request.courseId(),
                request.moduleId(),
                request.lessonId()
        );

        return mapper.toResponse(repository.save(progress));
    }

    @Transactional
    public LearningProgressResponse update(Long progressId, UpdateLearningProgressRequest request, User user) {
        LearningProgress progress = getProgressOrElseThrow(progressId);

        progress.setStudentId(user.getId());
        progress.setCourseId(request.courseId());
        progress.setModuleId(request.moduleId());
        progress.setLessonId(request.lessonId());

        return mapper.toResponse(repository.save(progress));
    }

    @Transactional
    public void delete(Long id, User user) {
        repository.delete(getProgressOrElseThrow(id));
    }

    private LearningProgress getProgressOrElseThrow(Long id){
        return repository.findByLessonId(id).orElseThrow(() -> {
            log.warn("Progress with id {} not found", id);
            return new ProgressNotFoundException(id);
        });
    }

    private int calculatePercentage(Long completedLessons, Long totalLessons) {
        if (totalLessons == null || totalLessons == 0) {
            return 0;
        }

        return (int) Math.round((completedLessons.doubleValue()/totalLessons.doubleValue()) * 100);
    }
}
