package com.kawevk.vkurso.enrollment.dtos;

import com.kawevk.vkurso.enrollment.Enrollment;
import com.kawevk.vkurso.enrollment.EnrollmentStatus;

import java.time.Instant;

public record EnrollmentResponse(
        Long id,
        Long studentId,
        Long courseId,
        EnrollmentStatus status,
        Instant enrolledAt,
        Instant cancelledAt,
        Instant createdAt,
        Instant updatedAt
) {
    public static EnrollmentResponse from(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getStudentId(),
                enrollment.getCourseId(),
                enrollment.getStatus(),
                enrollment.getEnrolledAt(),
                enrollment.getCancelledAt(),
                enrollment.getCreatedAt(),
                enrollment.getUpdatedAt()
        );
    }
}
