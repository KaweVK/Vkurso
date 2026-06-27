package com.kawevk.vkurso.course.dtos;

import com.kawevk.vkurso.course.Course;
import com.kawevk.vkurso.course.CourseLevel;
import com.kawevk.vkurso.course.CourseStatus;
import com.kawevk.vkurso.module.dtos.ModuleResponse;

import java.time.Instant;
import java.util.List;

public record CourseResponse(
        Long id,
        String title,
        String slug,
        String description,
        CourseLevel level,
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
                course.getLevel(),
                course.getStatus(),
                course.getInstructorId(),
                course.getModules().stream().map(ModuleResponse::from).toList(),
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }
}