package com.kawevk.vkurso.course.dtos;

import com.kawevk.vkurso.course.Course;
import com.kawevk.vkurso.course.CourseLevel;
import com.kawevk.vkurso.course.CourseStatus;
import com.kawevk.vkurso.module.dtos.ModuleResponse;
import com.kawevk.vkurso.user.dtos.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record CourseResponse(
        @Schema(
                description = "ID do curso",
                example = "1"
        )
        Long id,
        @Schema(
                description = "Título do curso",
                example = "Java com Spring Boot"
        )
        String title,
        @Schema(
                description = "Slug do curso",
                example = "java-com-spring-boot"
        )
        String slug,
        @Schema(
                description = "Descrição do curso",
                example = "Curso completo de Java com Spring Boot"
        )
        String description,
        @Schema(
                description = "Preço do curso",
                example = "199.99"
        )
        BigDecimal price,
        @Schema(
                description = "Nível do curso",
                example = "BEGINNER"
        )
        CourseLevel level,
        @Schema(
                description = "IDs das categorias do curso",
                example = "[1, 2, 3]"
        )
        Set<Long> categoryIds,
        @Schema(
                description = "Status do curso",
                example = "ACTIVE"
        )
        CourseStatus status,
        @Schema(
                description = "ID do instrutor do curso",
                example = "1"
        )
        Long instructorId,
        @Schema(
                description = "Módulos do curso",
                example = "[{...}, {...}]"
        )
        List<ModuleResponse> modules,
        @Schema(
                description = "Data de criação do curso",
                example = "2023-01-01T00:00:00Z"
        )
        Instant createdAt,
        @Schema(
                description = "Data de atualização do curso",
                example = "2023-01-01T00:00:00Z"
        )
        Instant updatedAt
) {
}