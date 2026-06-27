package com.kawevk.vkurso.module.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateModuleRequest(
        @NotBlank @Size(max = 150)
        String title,
        String description,
        @NotNull
        Long orderIndex
) {
}
