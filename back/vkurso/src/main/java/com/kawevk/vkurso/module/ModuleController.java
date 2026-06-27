package com.kawevk.vkurso.module;

import com.kawevk.vkurso.module.dtos.CreateModuleRequest;
import com.kawevk.vkurso.module.dtos.ModuleResponse;
import com.kawevk.vkurso.module.dtos.UpdateModuleRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/courses/{courseId}/modules")
public class ModuleController {

    private final ModuleService service;

    public ModuleController(ModuleService service) {
        this.service = service;
    }

    @GetMapping
    public Page<ModuleResponse> list(Pageable pageable) {
        return service.list(pageable);
    }

    @GetMapping("/{id}")
    public ModuleResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<ModuleResponse> create(@PathVariable Long courseId, @RequestBody @Valid CreateModuleRequest request, UriComponentsBuilder uriBuilder) {
        ModuleResponse created = service.create(courseId, request);
        URI location = uriBuilder.path("/api/courses/{courseId}/modules/{id}")
                .buildAndExpand(courseId, created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ModuleResponse update(@PathVariable Long id, @RequestBody @Valid UpdateModuleRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping("/{id}/lessons/{lessonId}/order")
    public ModuleResponse order(@PathVariable Long id, @PathVariable Long lessonId, @RequestParam Long newOrder) {
        return service.changeLessonOrder(id, lessonId, newOrder);
    }
}
