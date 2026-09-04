package com.kawevk.vkurso.courseCategory;

import com.kawevk.vkurso.courseCategory.dtos.CourseCategoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseCategoryMapper {

    CourseCategoryResponse toResponse(CourseCategory category);
}
