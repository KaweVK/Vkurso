package com.kawevk.vkurso.lesson.dtos;

import java.time.Instant;

public record LessonResponse(
        Long id,
        String title,
        String description,
        Long orderIndex,
        String videoUrl,
        Long durationSeconds,
        boolean freePreview,
        Long moduleId,
        Instant createdAt,
        Instant updatedAt
) {}
