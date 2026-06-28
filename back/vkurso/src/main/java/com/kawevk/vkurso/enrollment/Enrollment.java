package com.kawevk.vkurso.enrollment;

import com.kawevk.vkurso.shared.persistence.Auditable;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

@Entity
@Table(name = "enrollments")
@Getter
public class Enrollment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnrollmentStatus status;

    @Column(name = "enrolled_at", nullable = false)
    private Instant enrolledAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    public Enrollment() {}

    public Enrollment(Long studentId, Long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.status = EnrollmentStatus.ACTIVE;
        this.enrolledAt = Instant.now();
    }

    //Métodos de negócio

    public void cancel() {
        this.status = EnrollmentStatus.CANCELLED;
        this.cancelledAt = Instant.now();
    }

    public void reactivate() {
        this.status = EnrollmentStatus.ACTIVE;
        this.enrolledAt = Instant.now();
        this.cancelledAt = null;
    }

    public boolean isActive() {
        return status == EnrollmentStatus.ACTIVE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enrollment other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
