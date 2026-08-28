package com.kawevk.vkurso.learninProgress.dtos;

public record LearningProgressResponse(
        Long studentId,
        Long courseId,
        Long moduleId,
        Long lessonId
) {
}
