package com.kawevk.vkurso.lesson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("""
        SELECT COUNT(l)
        FROM Lesson l
        WHERE l.module.course.id = :courseId
    """)
    long countByCourseId(@Param("courseId") Long courseId);
}
