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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModuleService {

    private final ModuleRepository repository;
    private final CourseRepository courseRepository;

    public ModuleService(ModuleRepository repository, CourseRepository courseRepository) {
        this.repository = repository;
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public Page<ModuleResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(ModuleResponse::from);
    }

    @Transactional(readOnly = true)
    public ModuleResponse findById(Long id) {
        return ModuleResponse.from(getModuleOrThrow(id));
    }

    @Transactional
    public ModuleResponse create(Long courseId, CreateModuleRequest request, User user) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        ensureCanModify(course, user);

        Module module = new Module(
                request.title(),
                request.description(),
                request.orderIndex()
        );

        course.addModule(module);
        module.setCourse(course);

        return ModuleResponse.from(repository.save(module));
    }

    @Transactional
    public ModuleResponse update(Long id, UpdateModuleRequest request, User user) {
        Module module = getModuleOrThrow(id);

        ensureCanModify(module.getCourse(), user);

        module.setTitle(request.title());
        module.setDescription(request.description());
        module.setOrderIndex(request.orderIndex());

        return ModuleResponse.from(module);
    }

    @Transactional
    public void delete(Long id, User user) {
        Module module = getModuleOrThrow(id);

        ensureCanModify(module.getCourse(), user);
        //remove módulo
        module.getCourse().removeModule(module);

        repository.delete(module);
    }

    @Transactional
    public ModuleResponse changeLessonOrder(Long id, Long lessonId, Long newOrder, User user) {
        Module module = getModuleOrThrow(id);
        ensureCanModify(module.getCourse(), user);
        module.changeLessonOrder(lessonId, newOrder);
        return ModuleResponse.from(module);
    }

    private Module getModuleOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ModuleNotFoundException(id));
    }

    private void ensureCanModify(Course course, User user) {
        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isOwner = course.getInstructorId().equals(user.getId());
        if (!isAdmin && !isOwner) {
            throw new CourseRequestNotAllowed();
        }
    }
}
