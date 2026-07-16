package com.kawevk.vkurso.courseCategory.dtos;

import com.kawevk.vkurso.courseCategory.CourseCategory;

public record CourseCategoryResponse(
        Long id,
        String name
) {
    public static CourseCategoryResponse from(CourseCategory courseCategory) {
        return new CourseCategoryResponse(
                courseCategory.getId(),
                courseCategory.getName()
        );
    }
}
