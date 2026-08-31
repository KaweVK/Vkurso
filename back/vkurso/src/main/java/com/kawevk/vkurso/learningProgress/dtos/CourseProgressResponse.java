package com.kawevk.vkurso.learningProgress.dtos;

public record CourseProgressResponse(
        Long courseId,
        String title,
        String slug,
        String description,
        Long completedLessons,
        Long totalLessons,
        Integer progressPercentage,
        Status status
) {
    public enum Status {
        IN_PROGRESS,
        COMPLETED
    }
}