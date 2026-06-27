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
) {
    public static LessonResponse from(Lesson lesson) {
        return new LessonResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getOrderIndex(),
                lesson.getVideoKey(),
                lesson.getDurationSeconds(),
                lesson.isFreePreview(),
                lesson.getModule().getId(),
                lesson.getCreatedAt(),
                lesson.getUpdatedAt()
        );
    }

    public static LessonResponse from(Lesson lesson, VideoStorageService storage) {
        String videoUrl = lesson.getVideoKey() == null
                ? null
                : storage.presignedGetUrl(lesson.getVideoKey());
        return new LessonResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getOrderIndex(),
                videoUrl,
                lesson.getDurationSeconds(),
                lesson.isFreePreview(),
                lesson.getModule().getId(),
                lesson.getCreatedAt(),
                lesson.getUpdatedAt()
        );
    }
}
