package com.kawevk.vkurso.module;

import com.kawevk.vkurso.course.Course;
import com.kawevk.vkurso.course.CourseRepository;
import com.kawevk.vkurso.course.exceptions.CourseNotFoundException;
import com.kawevk.vkurso.module.dtos.CreateModuleRequest;
import com.kawevk.vkurso.module.dtos.ModuleResponse;
import com.kawevk.vkurso.module.dtos.UpdateModuleRequest;
import com.kawevk.vkurso.module.exceptions.ModuleNotFoundException;
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
    public ModuleResponse create(Long courseId, CreateModuleRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

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
    public ModuleResponse update(Long id, UpdateModuleRequest request) {
        Module module = getModuleOrThrow(id);

        module.setTitle(request.title());
        module.setDescription(request.description());
        module.setOrderIndex(request.orderIndex());

        return ModuleResponse.from(module);
    }

    @Transactional
    public void delete(Long id) {
        Module module = getModuleOrThrow(id);

        //remove módulo
        module.getCourse().removeModule(module);

        repository.delete(module);
    }

    private Module getModuleOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ModuleNotFoundException(id));
    }

    @Transactional
    public ModuleResponse changeLessonOrder(Long id, Long lessonId, Long newOrder) {
        Module module = getModuleOrThrow(id);
        module.changeLessonOrder(lessonId, newOrder);
        return ModuleResponse.from(module);
    }
}
