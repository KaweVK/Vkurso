package com.kawevk.vkurso.module;

import com.kawevk.vkurso.lesson.LessonMapper;
import com.kawevk.vkurso.module.dtos.ModuleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = LessonMapper.class)
public interface ModuleMapper {

    @Mapping(source = "course.id", target = "courseId")
    ModuleResponse toResponse (Module module);

}
