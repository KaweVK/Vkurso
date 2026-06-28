package com.kawevk.vkurso.enrollment;

import com.kawevk.vkurso.enrollment.dtos.EnrollmentResponse;
import com.kawevk.vkurso.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/courses/{courseId}/enrollments")
public class EnrollmentController {

    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EnrollmentResponse> enroll(@PathVariable Long courseId, @AuthenticationPrincipal User user, UriComponentsBuilder uri) {
        EnrollmentResponse created = service.enroll(user.getId(), courseId);
        URI location = uri.path("/api/courses/{courseId}/enrollments/me")
                .buildAndExpand(courseId)
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long courseId, @AuthenticationPrincipal User user) {
        service.cancel(user.getId(), courseId);
    }

    @GetMapping("/me")
    public EnrollmentResponse myEnrollment(@PathVariable Long courseId, @AuthenticationPrincipal User user) {
        return service.myEnrollment(user.getId(), courseId);
    }
}