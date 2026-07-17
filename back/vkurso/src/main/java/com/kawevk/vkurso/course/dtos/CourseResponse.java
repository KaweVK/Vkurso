package com.kawevk.vkurso.course.dtos;

import com.kawevk.vkurso.course.Course;
import com.kawevk.vkurso.course.CourseLevel;
import com.kawevk.vkurso.course.CourseStatus;
import com.kawevk.vkurso.module.dtos.ModuleResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public record CourseResponse(
        Long id,
        String title,
        String slug,
        String description,
        BigDecimal price,
        CourseLevel level,
        Set<Long> categoryIds,
        CourseStatus status,
        Long instructorId,
        List<ModuleResponse> modules,
        Instant createdAt,
        Instant updatedAt
) {
    // factory de mapeamento entity -> DTO (alternativa: um CourseMapper dedicado)
    public static CourseResponse from(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getSlug(),
                course.getDescription(),
                course.getPrice(),
                course.getLevel(),
                course.getCategoryIds(),
                course.getStatus(),
                course.getInstructorId(),
                course.getModules().stream().map(ModuleResponse::from).toList(),
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }
}