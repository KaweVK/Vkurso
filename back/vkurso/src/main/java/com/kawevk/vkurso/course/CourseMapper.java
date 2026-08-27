package com.kawevk.vkurso.course;

import com.kawevk.vkurso.course.dtos.CourseResponse;
import com.kawevk.vkurso.module.ModuleMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ModuleMapper.class)
public interface CourseMapper {

    CourseResponse toResponse(Course course);
}
