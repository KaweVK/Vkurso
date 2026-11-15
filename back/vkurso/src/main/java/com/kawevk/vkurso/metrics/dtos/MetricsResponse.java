package com.kawevk.vkurso.metrics.dtos;

public record MetricsResponse(
        Long totalPublishedCourses,
        Long totalUsers,
        Long totalEnrollments
) {}
