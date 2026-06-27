package com.kawevk.vkurso.course;

import aj.org.objectweb.asm.commons.Remapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findBySlug(String slug);

    boolean existsBySlug(String slug);

    Page<Course> findByStatus(CourseStatus status, Pageable pageable);

    Page<Course> findByInstructorId(Long instructorId, Pageable pageable);

    Page<Course> findAllByInstructorId(Long instructorId, Pageable pageable);
}