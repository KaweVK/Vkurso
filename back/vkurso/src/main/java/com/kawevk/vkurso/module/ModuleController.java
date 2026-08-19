package com.kawevk.vkurso.module;

import com.kawevk.vkurso.module.dtos.CreateModuleRequest;
import com.kawevk.vkurso.module.dtos.ModuleResponse;
import com.kawevk.vkurso.module.dtos.UpdateModuleRequest;
import com.kawevk.vkurso.user.User;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("api/courses/{courseId}/modules")
public class ModuleController {

    private final ModuleService service;

    public ModuleController(ModuleService service) {
        this.service = service;
    }

    @GetMapping
    public Page<ModuleResponse> list(Pageable pageable) {
        log.debug("Listing users with pagination: page {}, size {}", pageable.getPageNumber(), pageable.getPageSize());
        return service.list(pageable);
    }

    @GetMapping("/{id}")
    public ModuleResponse get(@PathVariable Long id) {
        log.debug("Finding module with ID: {}", id);
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<ModuleResponse> create(@PathVariable Long courseId, @RequestBody @Valid CreateModuleRequest request, UriComponentsBuilder uriBuilder, @AuthenticationPrincipal User user) {
        log.debug("Creating module in course with ID: {} with data: {}", courseId, request);
        ModuleResponse created = service.create(courseId, request, user);
        log.debug("Created module with ID: {}", created.id());
        URI location = uriBuilder.path("/api/courses/{courseId}/modules/{id}")
                .buildAndExpand(courseId, created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ModuleResponse update(@PathVariable Long id, @RequestBody @Valid UpdateModuleRequest request, @AuthenticationPrincipal User user) {
        log.debug("Updating module with ID: {}", id);
        return service.update(id, request, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        log.debug("Deleting module with ID: {}", id);
        service.delete(id, user);
        log.debug("Deleted module with ID: {}", id);
    }

    @PutMapping("/{id}/lessons/{lessonId}/order")
    public ModuleResponse order(@PathVariable Long id, @PathVariable Long lessonId, @RequestParam Long newOrder, @AuthenticationPrincipal User user) {
        log.debug("Ordering module with ID: {}", id);
        return service.changeLessonOrder(id, lessonId, newOrder, user);
    }
}
