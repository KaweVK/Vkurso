package com.kawevk.vkurso.lesson.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateLessonRequest(
        @NotBlank @Size(max = 150)
        String title,
        String description,
        @NotNull
        Long orderIndex
) {
}
