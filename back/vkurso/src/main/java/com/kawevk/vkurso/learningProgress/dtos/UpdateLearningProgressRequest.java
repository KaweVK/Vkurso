package com.kawevk.vkurso.learningProgress.dtos;

public record UpdateLearningProgressRequest(
        Long studentId,
        Long courseId,
        Long moduleId,
        Long lessonId
) {
}
