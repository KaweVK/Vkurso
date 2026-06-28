package com.kawevk.vkurso.enrollment;

import com.kawevk.vkurso.course.Course;
import com.kawevk.vkurso.course.CourseRepository;
import com.kawevk.vkurso.course.exceptions.CourseNotFoundException;
import com.kawevk.vkurso.enrollment.dtos.EnrollmentResponse;
import com.kawevk.vkurso.enrollment.exceptions.AlreadyEnrolledException;
import com.kawevk.vkurso.enrollment.exceptions.CourseNotPublishedException;
import com.kawevk.vkurso.enrollment.exceptions.NotEnrolledException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnrollmentService {

    private final EnrollmentRepository repository;
    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository repository, CourseRepository courseRepository) {
        this.repository = repository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public EnrollmentResponse enroll(Long studentId, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        if (!course.isPublished()) {
            throw new CourseNotPublishedException(courseId);
        }

        Enrollment enrollment = repository.findByStudentIdAndCourseId(studentId, courseId)
                .map(existing -> {
                    if (existing.isActive()) {
                        throw new AlreadyEnrolledException(courseId);
                    }
                    existing.reactivate(); // re-inscrição reaproveita a linha (soft delete)
                    return existing;
                })
                .orElseGet(() -> new Enrollment(studentId, courseId));

        return EnrollmentResponse.from(repository.save(enrollment));
    }

    @Transactional
    public void cancel(Long studentId, Long courseId) {
        Enrollment enrollment = repository.findByStudentIdAndCourseId(studentId, courseId)
                .filter(Enrollment::isActive)
                .orElseThrow(() -> new NotEnrolledException(courseId));

        enrollment.cancel();
    }

    @Transactional(readOnly = true)
    public EnrollmentResponse myEnrollment(Long studentId, Long courseId) {
        Enrollment enrollment = repository.findByStudentIdAndCourseId(studentId, courseId)
                .filter(Enrollment::isActive)
                .orElseThrow(() -> new NotEnrolledException(courseId));

        return EnrollmentResponse.from(enrollment);
    }

    @Transactional(readOnly = true)
    public boolean isEnrolled(Long studentId, Long courseId) {
        return repository.existsByStudentIdAndCourseIdAndStatus(
                studentId, courseId, EnrollmentStatus.ACTIVE);
    }

}
