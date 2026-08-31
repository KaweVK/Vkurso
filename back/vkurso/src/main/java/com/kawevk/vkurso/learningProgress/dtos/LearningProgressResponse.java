package com.kawevk.vkurso.learningProgress.dtos;

public record LearningProgressResponse(
        Long studentId,
        Long courseId,
        Long moduleId,
        Long lessonId
) {
}
