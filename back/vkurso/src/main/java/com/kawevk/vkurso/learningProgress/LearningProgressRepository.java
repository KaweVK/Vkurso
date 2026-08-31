package com.kawevk.vkurso.learningProgress;

import com.kawevk.vkurso.learningProgress.dtos.LearningProgressResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LearningProgressRepository extends JpaRepository<LearningProgress, Long> {

    Optional<LearningProgress> findByLessonId(Long lessonId);

    Page<LearningProgressResponse> findAllByStudentId(Long id, Pageable pageable);

    @Query("""
        SELECT lp.courseId, COUNT(lp.id)
        FROM LearningProgress lp
        WHERE lp.studentId = :studentId
        GROUP BY lp.courseId
    """)
    Page<Object[]> countCompletedLessonsByStudent(
            @Param("studentId") Long studentId,
            Pageable pageable
    );
}
