package com.kawevk.vkurso.learninProgress.dtos;

public record CreateLearningProgressRequest(
        Long studentId,
        Long courseId,
        Long moduleId,
        Long lessonId
) {

}
