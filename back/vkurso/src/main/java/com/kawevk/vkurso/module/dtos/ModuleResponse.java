package com.kawevk.vkurso.module.dtos;

import com.kawevk.vkurso.lesson.dtos.LessonResponse;
import com.kawevk.vkurso.module.Module;


import java.time.Instant;
import java.util.List;

public record ModuleResponse(
        Long id,
        String title,
        String description,
        Long orderIndex,
        Long courseId,
        List<LessonResponse> lessons,
        Instant createdAt,
        Instant updatedAt
) {
    public static ModuleResponse from(Module module) {
        return new ModuleResponse(
                module.getId(),
                module.getTitle(),
                module.getDescription(),
                module.getOrderIndex(),
                module.getCourse().getId(),
                module.getLessons().stream().map(LessonResponse::from).toList(),
                module.getCreatedAt(),
                module.getUpdatedAt()
        );
    }
}
