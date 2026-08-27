package com.kawevk.vkurso.course;

import com.kawevk.vkurso.course.dtos.CourseResponse;
import com.kawevk.vkurso.course.dtos.CreateCourseRequest;
import com.kawevk.vkurso.course.dtos.UpdateCourseRequest;
import com.kawevk.vkurso.course.exceptions.CourseNotFoundException;
import com.kawevk.vkurso.course.exceptions.CourseRequestNotAllowed;
import com.kawevk.vkurso.course.exceptions.DuplicateSlugException;
import com.kawevk.vkurso.courseCategory.CourseCategory;
import com.kawevk.vkurso.courseCategory.CourseCategoryRepository;
import com.kawevk.vkurso.user.Role;
import com.kawevk.vkurso.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CourseService {

    private final CourseRepository repository;
    private final CourseCategoryRepository courseCategoryRepository;
    private final CacheManager cacheManager;
    private final CourseMapper mapper;

    public CourseService(CourseRepository repository, CourseCategoryRepository courseCategoryRepository, CacheManager cacheManager, CourseMapper mapper) {
        this.repository = repository;
        this.courseCategoryRepository = courseCategoryRepository;
        this.cacheManager = cacheManager;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public Page<CourseResponse> list(Pageable pageable) {
        try {
            return repository.findAll(pageable).map(mapper::toResponse);
        } catch (CourseNotFoundException e) {
            log.warn("Course not found!", e);
            throw new CourseNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public Page<CourseResponse> listByInstructor(Long instructorId, Pageable pageable) {
        try {
            return repository.findAllByInstructorId(instructorId, pageable).map(mapper::toResponse);
        } catch (CourseNotFoundException e) {
            log.warn("Course not found!", e);
            throw new CourseNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public CourseResponse get(Long id) {
        return mapper.toResponse(getCourseOrThrow(id));
    }

    @Cacheable(value = "courses", key = "#slug")
    @Transactional(readOnly = true)
    public CourseResponse getBySlug(String slug) {
        return mapper.toResponse(repository.findBySlug(slug).orElseThrow(() -> {
            log.warn("Course with slug '{}' not found!", slug);
            return new CourseNotFoundException(slug);
        }));
    }

    @Transactional(readOnly = true)
    public Page<CourseResponse> search(String search, Long categoryId, Pageable pageable) {
        String normalizedSearch = search == null ? null : search.trim();
        Page<Course> courses = repository.search(normalizedSearch, categoryId, pageable);

        return courses.map(mapper::toResponse);
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

        return mapper.toResponse(repository.save(course));
    }

    @Transactional
    @CacheEvict(value = "courses", key = "#id")
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

        CourseResponse response = mapper.toResponse(course);

        evictCourseCache(oldSlug);

        if (!oldSlug.equals(novoSlug)) {
            evictCourseCache(novoSlug);
        }

        return response;
    }

    @Transactional
    @CacheEvict(value = "courses", key = "#id")
    public CourseResponse publish(Long id, User user) {
        Course course = getCourseOrThrow(id);
        ensureCanModify(course, user);
        course.publish();
        evictCourseCache(course.getSlug());
        return mapper.toResponse(course);
    }

    @Transactional
    @CacheEvict(value = "courses", key = "#id")
    public CourseResponse archive(Long id, User user) {
        Course course = getCourseOrThrow(id);
        ensureCanModify(course, user);
        course.archive();
        evictCourseCache(course.getSlug());
        return mapper.toResponse(course);
    }

    @Transactional
    @CacheEvict(value = "courses", key = "#id")
    public CourseResponse addCategory(Long id, Long idCategory, User user) {
        Course course = getCourseOrThrow(id);
        ensureCanModify(course, user);
        course.addCategory(idCategory);
        evictCourseCache(course.getSlug());
        return mapper.toResponse(course);
    }

    @Transactional
    @CacheEvict(value = "courses", key = "#id")
    public CourseResponse removeCategory(Long id, Long idCategory, User user) {
        Course course = getCourseOrThrow(id);
        ensureCanModify(course, user);
        course.removeCategory(idCategory);
        evictCourseCache(course.getSlug());
        return mapper.toResponse(course);
    }

    @Transactional
    @CacheEvict(value = "courses", key = "#id")
    public void delete(Long id, User user) {
        Course course = getCourseOrThrow(id);
        ensureCanModify(course, user);
        evictCourseCache(course.getSlug());
        repository.delete(course);
    }

    @Transactional
    public void deleteByInstructor(Long instructorId) {
        repository.deleteAll(repository.findByInstructorId(instructorId, Pageable.unpaged()));
    }

    @Transactional
    @CacheEvict(value = "courses", key = "#id")
    public CourseResponse changeModuleOrder(Long id, Long moduleId, int newOrder, User user) {
        Course course = getCourseOrThrow(id);
        ensureCanModify(course, user);
        course.changeModuleOrder(moduleId, newOrder);
        evictCourseCache(course.getSlug());
        return mapper.toResponse(course);
    }

    private Course getCourseOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Course with ID '{}' not found!", id);
                    return new CourseNotFoundException(id);
                });
    }

    private CourseCategory getCourseCategoryOrThrow(Long id) {
        return courseCategoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Course category with ID '{}' not found!", id);
                    return new CourseNotFoundException(id);
                });
    }

    private void evictCourseCache(String slug) {
        Cache cache = cacheManager.getCache("courses");

        if (cache != null) {
            cache.evict(slug);
        }
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
