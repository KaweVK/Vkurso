package com.kawevk.vkurso.lesson.dtos;

import com.kawevk.vkurso.lesson.Lesson;
import com.kawevk.vkurso.shared.storage.VideoStorageService;

import java.time.Duration;
import java.time.Instant;

public record LessonResponse(
        Long id,
        String title,
        String description,
        Long orderIndex,
        String videoUrl,
        Duration durationSeconds,
        boolean freePreview,
        Long moduleId,
        Instant createdAt,
        Instant updatedAt
) {}
