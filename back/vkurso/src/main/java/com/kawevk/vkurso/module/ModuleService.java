package com.kawevk.vkurso.module;

import com.kawevk.vkurso.course.Course;
import com.kawevk.vkurso.course.CourseRepository;
import com.kawevk.vkurso.course.exceptions.CourseNotFoundException;
import com.kawevk.vkurso.course.exceptions.CourseRequestNotAllowed;
import com.kawevk.vkurso.module.dtos.CreateModuleRequest;
import com.kawevk.vkurso.module.dtos.ModuleResponse;
import com.kawevk.vkurso.module.dtos.UpdateModuleRequest;
import com.kawevk.vkurso.module.exceptions.ModuleNotFoundException;
import com.kawevk.vkurso.user.Role;
import com.kawevk.vkurso.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ModuleService {

    private final ModuleRepository repository;
    private final CourseRepository courseRepository;
    private final ModuleMapper mapper;

    public ModuleService(ModuleRepository repository, CourseRepository courseRepository, ModuleMapper mapper) {
        this.repository = repository;
        this.courseRepository = courseRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public Page<ModuleResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Cacheable(value = "modules", key = "#id")
    @Transactional(readOnly = true)
    public ModuleResponse findById(Long id) {
        return mapper.toResponse(getModuleOrThrow(id));
    }

    @Transactional
    @CacheEvict(value = "courses", allEntries = true)
    public ModuleResponse create(Long courseId, CreateModuleRequest request, User user) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.warn("Course not found! {}", courseId);
                    return new CourseNotFoundException(courseId);
                });

        ensureCanModify(course, user);

        Module module = new Module(
                request.title(),
                request.description(),
                request.orderIndex()
        );

        course.addModule(module);
        module.setCourse(course);

        return mapper.toResponse(repository.save(module));
    }

    @Transactional
    @CacheEvict(value = {"modules", "courses"}, allEntries = true)
    public ModuleResponse update(Long id, UpdateModuleRequest request, User user) {
        Module module = getModuleOrThrow(id);

        ensureCanModify(module.getCourse(), user);

        module.setTitle(request.title());
        module.setDescription(request.description());
        module.setOrderIndex(request.orderIndex());

        return mapper.toResponse(module);
    }

    @Transactional
    @CacheEvict(value = {"modules", "courses"}, allEntries = true)
    public void delete(Long id, User user) {
        Module module = getModuleOrThrow(id);

        ensureCanModify(module.getCourse(), user);
        //remove módulo
        module.getCourse().removeModule(module);

        repository.delete(module);
    }

    @Transactional
    @CacheEvict(value = {"modules", "courses"}, allEntries = true)
    public ModuleResponse changeLessonOrder(Long id, Long lessonId, Long newOrder, User user) {
        Module module = getModuleOrThrow(id);
        ensureCanModify(module.getCourse(), user);
        module.changeLessonOrder(lessonId, newOrder);
        return mapper.toResponse(module);
    }

    private Module getModuleOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Module not found! {}", id);
                    return new ModuleNotFoundException(id);
                });
    }

    private void ensureCanModify(Course course, User user) {
        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isOwner = course.getInstructorId().equals(user.getId());
        if (!isAdmin && !isOwner) {
            log.warn("User {} attempted to modify course {} without permission", user.getId(), course.getId());
            throw new CourseRequestNotAllowed();
        }
    }
}
