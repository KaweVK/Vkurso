package com.kawevk.vkurso.course;

import com.kawevk.vkurso.course.dtos.CourseResponse;
import com.kawevk.vkurso.course.dtos.CreateCourseRequest;
import com.kawevk.vkurso.course.dtos.UpdateCourseRequest;
import com.kawevk.vkurso.course.exceptions.CourseNotFoundException;
import com.kawevk.vkurso.course.exceptions.DuplicateSlugException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseService {

    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<CourseResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(CourseResponse::from);
    }

    @Transactional(readOnly = true)
    public CourseResponse findById(Long id) {
        return CourseResponse.from(getCourseOrThrow(id));
    }

    @Transactional
    public CourseResponse create(CreateCourseRequest request) {
        Course course = new Course(
                request.title(),
                request.description(),
                request.level(),
                request.instructorId()
        );

        if (repository.existsBySlug(course.getSlug())) {
            throw new DuplicateSlugException(course.getSlug());
        }

        return CourseResponse.from(repository.save(course));
    }

    @Transactional
    public CourseResponse update(Long id, UpdateCourseRequest request) {
        Course course = getCourseOrThrow(id);

        String novoSlug = course.toSlug(request.title());

        if (!novoSlug.equals(course.getSlug()) && repository.existsBySlug(novoSlug)) {
            throw new DuplicateSlugException(novoSlug);
        }

        course.setTitle(request.title());
        course.setSlug(novoSlug);
        course.setDescription(request.description());
        course.setLevel(request.level());

        return CourseResponse.from(course);
    }

    @Transactional
    public CourseResponse publish(Long id) {
        Course course = getCourseOrThrow(id);
        course.publish();
        return CourseResponse.from(course);
    }

    @Transactional
    public CourseResponse archive(Long id) {
        Course course = getCourseOrThrow(id);
        course.archive();
        return CourseResponse.from(course);
    }

    @Transactional
    public void delete(Long id) {
        Course course = getCourseOrThrow(id);
        repository.delete(course);
    }

    private Course getCourseOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));
    }

    @Transactional
    public CourseResponse changeModuleOrder(Long Id, Long moduleId, int newOrder) {
        Course course = getCourseOrThrow(Id);
        course.changeModuleOrder(moduleId, newOrder);
        return CourseResponse.from(course);
    }
}