package com.kawevk.vkurso.course.dtos;

import com.kawevk.vkurso.course.CourseLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateCourseRequest(
        @Schema(
                description = "Título do curso",
                example = "Java com Spring Boot"
        )
        @NotBlank @Size(max = 150)
        String title,
        @Schema(
                description = "Descrição do curso",
                example = "Curso completo de Java com Spring Boot"
        )
        String description,
        @Schema(
                description = "Nível de facilidade do curso",
                example = "BEGINNER"
        )
        @NotNull
        CourseLevel level,
        @Schema(
                description = "Preço do curso",
                example = "199.99"
        )
        @NotNull
        BigDecimal price
) {}