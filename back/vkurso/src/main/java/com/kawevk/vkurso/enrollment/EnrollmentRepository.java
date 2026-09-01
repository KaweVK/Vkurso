package com.kawevk.vkurso.enrollment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    boolean existsByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, EnrollmentStatus status);

    @Query("""
    SELECT e.courseId
    FROM Enrollment e
    WHERE e.status = "ACTIVE"
    GROUP BY e.courseId
    ORDER BY COUNT(e.id) DESC
    """)
    List<Long> findTop5MostEnrolledCourseIds(Pageable pageable);
}
