package com.kawevk.vkurso.learninProgress;

import com.kawevk.vkurso.learninProgress.dtos.CreateLearningProgressRequest;
import com.kawevk.vkurso.learninProgress.dtos.LearningProgressResponse;
import com.kawevk.vkurso.learninProgress.dtos.UpdateLearningProgressRequest;
import com.kawevk.vkurso.learninProgress.exceptions.ProgressNotFoundException;
import com.kawevk.vkurso.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class LearningProgressService {

    private final LearningProgressRepository repository;
    private final LearningProgressMapper mapper;

    public LearningProgressService(LearningProgressRepository repository, LearningProgressMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public Page<LearningProgressResponse> findAll(Pageable pageable, User user) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public LearningProgressResponse findById(Long id) {
        return mapper.toResponse(getProgressOrElseThrow(id));
    }

    @Transactional
    public LearningProgressResponse create(CreateLearningProgressRequest request) {
        LearningProgress progress = new LearningProgress(
                request.studentId(),
                request.courseId(),
                request.moduleId(),
                request.lessonId()
        );

        return mapper.toResponse(repository.save(progress));
    }

    @Transactional
    public LearningProgressResponse update(Long progressId, UpdateLearningProgressRequest request) {
        LearningProgress progress = getProgressOrElseThrow(progressId);

        progress.setStudentId(request.studentId());
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
        return repository.findById(id).orElseThrow(() -> {
            log.warn("Progress with id {} not found", id);
            return new ProgressNotFoundException(id);
        });
    }
}
