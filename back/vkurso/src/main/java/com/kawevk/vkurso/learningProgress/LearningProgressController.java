package com.kawevk.vkurso.learningProgress;

import com.kawevk.vkurso.learningProgress.dtos.CourseProgressResponse;
import com.kawevk.vkurso.learningProgress.dtos.CreateLearningProgressRequest;
import com.kawevk.vkurso.learningProgress.dtos.LearningProgressResponse;
import com.kawevk.vkurso.learningProgress.dtos.UpdateLearningProgressRequest;
import com.kawevk.vkurso.user.User;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("api/progress")
public class LearningProgressController {

    private final LearningProgressService service;

    public LearningProgressController(LearningProgressService service) {
        this.service = service;
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<LearningProgressResponse> findByLesson(
            @PathVariable Long lessonId,
            @AuthenticationPrincipal User user
    ) {
        LearningProgressResponse progress = service.findByLesson(lessonId);
        return ResponseEntity.ok().body(progress);
    }

    @GetMapping()
    public ResponseEntity<Page<CourseProgressResponse>> findStudentProgress(
            Pageable pageable,
            @AuthenticationPrincipal User user
    ) {
        Page<CourseProgressResponse> page = service.getStudentCourses(user, pageable);
        return ResponseEntity.ok().body(page);
    }

    @PostMapping
    public ResponseEntity<LearningProgressResponse> create(
            @RequestBody @Valid CreateLearningProgressRequest request,
            UriComponentsBuilder uriBuilder,
            @AuthenticationPrincipal User user
    ) {
        LearningProgressResponse created = service.create(request, user);
        URI location = uriBuilder.path("/api/progress/{id}")
                .buildAndExpand(created)
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LearningProgressResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateLearningProgressRequest request,
            @AuthenticationPrincipal User user
    ) {
        LearningProgressResponse updated = service.update(id, request, user);
        return ResponseEntity.ok().body(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LearningProgressResponse> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        service.delete(id, user);
        return ResponseEntity.noContent().build();
    }

}
