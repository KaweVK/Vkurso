package com.kawevk.vkurso.learningProgress.dtos;

public record CreateLearningProgressRequest(
        Long courseId,
        Long moduleId,
        Long lessonId
) {

}
