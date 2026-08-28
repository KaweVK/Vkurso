package com.kawevk.vkurso.learninProgress;

import com.kawevk.vkurso.learninProgress.dtos.CreateLearningProgressRequest;
import com.kawevk.vkurso.learninProgress.dtos.LearningProgressResponse;
import com.kawevk.vkurso.learninProgress.dtos.UpdateLearningProgressRequest;
import com.kawevk.vkurso.user.User;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping
    public ResponseEntity<LearningProgressResponse> findById(
            Long id,
            @AuthenticationPrincipal User user
    ) {
        LearningProgressResponse progress = service.findById(id);
        return ResponseEntity.ok().body(progress);
    }

    @PostMapping
    public ResponseEntity<LearningProgressResponse> create(
            @RequestBody @Valid CreateLearningProgressRequest request,
            UriComponentsBuilder uriBuilder,
            @AuthenticationPrincipal User user
    ) {
        LearningProgressResponse created = service.create(request);
        URI location = uriBuilder.path("/api/courses/{id}")
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
        LearningProgressResponse updated = service.update(id, request);
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
