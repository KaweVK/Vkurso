package com.kawevk.vkurso.course;

import com.kawevk.vkurso.course.dtos.CourseResponse;
import com.kawevk.vkurso.course.dtos.CreateCourseRequest;
import com.kawevk.vkurso.course.dtos.UpdateCourseRequest;
import com.kawevk.vkurso.course.exceptions.CourseNotFoundException;
import com.kawevk.vkurso.course.exceptions.CourseRequestNotAllowed;
import com.kawevk.vkurso.course.exceptions.DuplicateSlugException;
import com.kawevk.vkurso.courseCategory.CourseCategory;
import com.kawevk.vkurso.courseCategory.CourseCategoryRepository;
import com.kawevk.vkurso.enrollment.EnrollmentRepository;
import com.kawevk.vkurso.user.Role;
import com.kawevk.vkurso.user.User;
import com.kawevk.vkurso.user.UserRepository;
import com.kawevk.vkurso.user.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseService {

    private final CourseRepository repository;
    private final CourseCategoryRepository courseCategoryRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseMapper mapper;

    public CourseService(CourseRepository repository, CourseCategoryRepository courseCategoryRepository, EnrollmentRepository enrollmentRepository, UserRepository userRepository, CourseMapper mapper) {
        this.repository = repository;
        this.courseCategoryRepository = courseCategoryRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public Page<CourseResponse> list(Pageable pageable) {
        try {
            return repository.findAll(pageable).map(this::toResponse);
        } catch (CourseNotFoundException e) {
            log.warn("Course not found!", e);
            throw new CourseNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public Page<CourseResponse> findByInstructor(Long instructorId, CourseStatus status, Pageable pageable) {
        Page<CourseResponse> courses;

        if (status != null) {
            courses = repository.findByInstructorIdAndStatus(
                    instructorId,
                    status,
                    pageable
            ).map(this::toResponse);
        } else {
            courses = repository.findByInstructorId(
                    instructorId,
                    pageable
            ).map(this::toResponse);
        }

        return courses;
    }

    @Transactional(readOnly = true)
    public CourseResponse get(Long id) {
        return toResponse(getCourseOrThrow(id));
    }

    @Cacheable(value = "courses", key = "#slug")
    @Transactional(readOnly = true)
    public CourseResponse getBySlug(String slug) {
        Course course = repository.findBySlug(slug).orElseThrow(() -> {
            log.warn("Course with slug '{}' not found!", slug);
            return new CourseNotFoundException(slug);
        });
        return toResponse(course);
    }

    @Transactional(readOnly = true)
    public Page<CourseResponse> search(String search, Long categoryId, Pageable pageable) {
        String normalizedSearch = search == null ? null : search.trim();
        Page<Course> courses = repository.search(normalizedSearch, categoryId, pageable);

        return courses.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getFeaturedCourses() {
        List<Long> courseIds =
                enrollmentRepository.findTop5MostEnrolledCourseIds(
                        PageRequest.of(0, 5)
                );

        Map<Long, Course> courses = repository
                .findAllById(courseIds)
                .stream()
                .collect(Collectors.toMap(
                        Course::getId,
                        Function.identity()
                ));

        return courseIds.stream()
                .map(courses::get)
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CourseResponse create(CreateCourseRequest request, User user) {
        if (user.getRole() != Role.INSTRUCTOR) {
            throw new CourseRequestNotAllowed();
        }

        Course course = new Course(
                request.title(),
                request.description(),
                request.price(),
                request.level(),
                user.getId()
        );

        if (repository.existsBySlug(course.getSlug())) {
            log.warn("Course created with slug '{}' already exists!", course.getSlug());
            throw new DuplicateSlugException(course.getSlug());
        }

        return toResponse(repository.save(course));
    }

    @Transactional
    @CacheEvict(value = "courses", allEntries = true)
    public CourseResponse update(Long id, UpdateCourseRequest request, User user) {
        Course course = getCourseOrThrow(id);

        ensureCanModify(course, user);

        String oldSlug = course.getSlug();
        String novoSlug = course.toSlug(request.title());

        if (!novoSlug.equals(course.getSlug()) && repository.existsBySlug(novoSlug)) {
            log.warn("Course updated with slug '{}' already exists!", novoSlug);
            throw new DuplicateSlugException(novoSlug);
        }

        course.setTitle(request.title());
        course.setSlug(novoSlug);
        course.setDescription(request.description());
        course.setLevel(request.level());
        course.setPrice(request.price());

        CourseResponse response = toResponse(course);

        return response;
    }

    @Transactional
    @CacheEvict(value = "courses", allEntries = true)
    public CourseResponse publish(Long id, User user) {
        Course course = getCourseOrThrow(id);
        ensureCanModify(course, user);
        course.publish();
        return toResponse(course);
    }

    @Transactional
    @CacheEvict(value = "courses", allEntries = true)
    public CourseResponse archive(Long id, User user) {
        Course course = getCourseOrThrow(id);
        ensureCanModify(course, user);
        course.archive();
        return toResponse(course);
    }

    @Transactional
    @CacheEvict(value = "courses", allEntries = true)
    public CourseResponse addCategory(Long id, Long idCategory, User user) {
        Course course = getCourseOrThrow(id);
        ensureCanModify(course, user);
        course.addCategory(idCategory);
        return toResponse(course);
    }

    @Transactional
    @CacheEvict(value = "courses", allEntries = true)
    public CourseResponse removeCategory(Long id, Long idCategory, User user) {
        Course course = getCourseOrThrow(id);
        ensureCanModify(course, user);
        course.removeCategory(idCategory);
        return toResponse(course);
    }

    @Transactional
    @CacheEvict(value = "courses", allEntries = true)
    public void delete(Long id, User user) {
        Course course = getCourseOrThrow(id);
        ensureCanModify(course, user);
        repository.delete(course);
    }

    @Transactional
    @CacheEvict(value = "courses", allEntries = true)
    public void deleteByInstructor(Long instructorId) {
        repository.deleteAll(repository.findByInstructorId(instructorId, Pageable.unpaged()));
    }

    @Transactional
    @CacheEvict(value = "courses", allEntries = true)
    public CourseResponse changeModuleOrder(Long id, Long moduleId, int newOrder, User user) {
        Course course = getCourseOrThrow(id);
        ensureCanModify(course, user);
        course.changeModuleOrder(moduleId, newOrder);
        return toResponse(course);
    }

    private CourseResponse toResponse(Course course) {

        User instructor = userRepository.findById(course.getInstructorId())
                .orElseThrow(() -> new UserNotFoundException(course.getInstructorId()));

        List<CourseCategory> categories =
                courseCategoryRepository.findAllById(course.getCategoryIds());

        return mapper.toResponse(course, instructor, categories);
    }

    private Course getCourseOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Course with ID '{}' not found!", id);
                    return new CourseNotFoundException(id);
                });
    }

    private void ensureCanModify(Course course, User user) {
        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isOwner = course.getInstructorId().equals(user.getId());
        if (!isAdmin && !isOwner) {
            log.warn("User with ID '{}' is not the owner of course with ID '{}' or admin", user.getId(), course.getId());
            throw new CourseRequestNotAllowed();
        }
    }
}
