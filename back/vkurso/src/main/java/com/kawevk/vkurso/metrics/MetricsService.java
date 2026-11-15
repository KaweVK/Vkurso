package com.kawevk.vkurso.metrics;

import com.kawevk.vkurso.course.CourseRepository;
import com.kawevk.vkurso.enrollment.Enrollment;
import com.kawevk.vkurso.enrollment.EnrollmentRepository;
import com.kawevk.vkurso.metrics.dtos.MetricsResponse;
import com.kawevk.vkurso.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MetricsService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    public MetricsService(CourseRepository courseRepository, UserRepository userRepository, EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public MetricsResponse getMetrics() {
        Long totalPublishedCourses = courseRepository.countPublishedCourses();
        Long totalUsers = userRepository.count();
        Long totalEnrollments = enrollmentRepository.totalActiveEnrollments();

        return new MetricsResponse(totalPublishedCourses, totalUsers, totalEnrollments);
    }
}
