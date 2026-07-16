package com.kawevk.vkurso.course;

import com.kawevk.vkurso.course.dtos.CourseResponse;
import com.kawevk.vkurso.course.dtos.CreateCourseRequest;
import com.kawevk.vkurso.course.dtos.UpdateCourseRequest;
import com.kawevk.vkurso.course.exceptions.CourseNotFoundException;
import com.kawevk.vkurso.course.exceptions.CourseRequestNotAllowed;
import com.kawevk.vkurso.course.exceptions.DuplicateSlugException;
import com.kawevk.vkurso.user.Role;
import com.kawevk.vkurso.user.User;
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
        try {
            return repository.findAll(pageable).map(CourseResponse::from);
        } catch (CourseNotFoundException e) {
            throw new CourseNotFoundException();
        }
    }

    @Transactional
    public Page<CourseResponse> listByInstructor(Long instructorId, Pageable pageable) {
        try {
            return repository.findAllByInstructorId(instructorId, pageable).map(CourseResponse::from);
        } catch (CourseNotFoundException e) {
            throw new CourseNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public CourseResponse get(Long id) {
        return CourseResponse.from(getCourseOrThrow(id));
    }

    public CourseResponse getBySlug(String slug) {
        return CourseResponse.from(repository.findBySlug(slug).orElseThrow(() -> new CourseNotFoundException(slug)));
    }

    @Transactional
    public CourseResponse create(CreateCourseRequest request, User user) {
        Course course = new Course(
                request.title(),
                request.description(),
                request.price(),
                request.level(),
                user.getId()
        );

        if (repository.existsBySlug(course.getSlug())) {
            throw new DuplicateSlugException(course.getSlug());
        }

        return CourseResponse.from(repository.save(course));
    }

    @Transactional
    public CourseResponse update(Long id, UpdateCourseRequest request, User user) {
        Course course = getCourseOrThrow(id);

        ensureCanModify(course, user);

        String novoSlug = course.toSlug(request.title());

        if (!novoSlug.equals(course.getSlug()) && repository.existsBySlug(novoSlug)) {
            throw new DuplicateSlugException(novoSlug);
        }

        course.setTitle(request.title());
        course.setSlug(novoSlug);
        course.setDescription(request.description());
        course.setLevel(request.level());
        course.setPrice(request.price());

        return CourseResponse.from(course);
    }

    @Transactional
    public CourseResponse publish(Long id, User user) {
        Course course = getCourseOrThrow(id);
        ensureCanModify(course, user);
        course.publish();
        return CourseResponse.from(course);
    }

    @Transactional
    public CourseResponse archive(Long id, User user) {
        Course course = getCourseOrThrow(id);
        ensureCanModify(course, user);
        course.archive();
        return CourseResponse.from(course);
    }

    @Transactional
    public void delete(Long id, User user) {
        Course course = getCourseOrThrow(id);
        ensureCanModify(course, user);
        repository.delete(course);
    }

    @Transactional
    public void deleteByInstructor(Long instructorId) {
        repository.deleteAll(repository.findByInstructorId(instructorId, Pageable.unpaged()));
    }

    @Transactional
    public CourseResponse changeModuleOrder(Long Id, Long moduleId, int newOrder, User user) {
        Course course = getCourseOrThrow(Id);
        ensureCanModify(course, user);
        course.changeModuleOrder(moduleId, newOrder);
        return CourseResponse.from(course);
    }

    private Course getCourseOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));
    }

    private void ensureCanModify(Course course, User user) {
        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isOwner = course.getInstructorId().equals(user.getId());
        if (!isAdmin && !isOwner) {
            throw new CourseRequestNotAllowed();
        }
    }
}