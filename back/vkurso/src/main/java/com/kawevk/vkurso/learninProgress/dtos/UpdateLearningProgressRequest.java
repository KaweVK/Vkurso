package com.kawevk.vkurso.learninProgress.dtos;

public record UpdateLearningProgressRequest(
        Long studentId,
        Long courseId,
        Long moduleId,
        Long lessonId
) {
}
