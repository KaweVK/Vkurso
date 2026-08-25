package com.kawevk.vkurso.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("""
    SELECT c
    FROM Course c
    WHERE (:search IS NULL OR :search = ''
       OR LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')))
      AND (:categoryId IS NULL OR :categoryId MEMBER OF c.categoryIds)
    """)
    Page<Course> search(
            @Param("search") String search,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    Optional<Course> findBySlug(String slug);

    boolean existsBySlug(String slug);

    Page<Course> findByStatus(CourseStatus status, Pageable pageable);

    Page<Course> findByInstructorId(Long instructorId, Pageable pageable);

    Page<Course> findAllByInstructorId(Long instructorId, Pageable pageable);
}