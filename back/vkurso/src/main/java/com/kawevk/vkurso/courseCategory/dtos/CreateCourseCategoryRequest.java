package com.kawevk.vkurso.courseCategory.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.checkerframework.common.aliasing.qual.Unique;

public record CreateCourseCategoryRequest(
        @NotNull
        @Size(max = 100)
        @Unique
        String name
) {
}
