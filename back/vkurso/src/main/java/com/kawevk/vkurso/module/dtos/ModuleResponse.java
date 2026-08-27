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
) {}
