package com.kawevk.vkurso.course;

import com.kawevk.vkurso.course.dtos.CourseResponse;
import com.kawevk.vkurso.courseCategory.CourseCategory;
import com.kawevk.vkurso.courseCategory.CourseCategoryMapper;
import com.kawevk.vkurso.module.ModuleMapper;
import com.kawevk.vkurso.user.User;
import com.kawevk.vkurso.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ModuleMapper.class, UserMapper.class, CourseCategoryMapper.class})
public interface CourseMapper {

    @Mapping(target = "id", source = "course.id")
    @Mapping(target = "createdAt", source = "course.createdAt")
    @Mapping(target = "updatedAt", source = "course.updatedAt")
    @Mapping(target = "instructor", source = "instructor")
    @Mapping(target = "categories", source = "categories")
    CourseResponse toResponse(Course course, User instructor, List<CourseCategory> categories);
}
