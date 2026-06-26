package com.kawevk.vkurso.course;

import com.kawevk.vkurso.course.dtos.CourseResponse;
import com.kawevk.vkurso.course.dtos.CreateCourseRequest;
import com.kawevk.vkurso.course.dtos.UpdateCourseRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @GetMapping
    public Page<CourseResponse> list(Pageable pageable) {
        return service.list(pageable);
    }

    @GetMapping("/{id}")
    public CourseResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<CourseResponse> create(@RequestBody @Valid CreateCourseRequest request, UriComponentsBuilder uriBuilder) {
        CourseResponse created = service.create(request);
        URI location = uriBuilder.path("/api/courses/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public CourseResponse update(@PathVariable Long id, @RequestBody @Valid UpdateCourseRequest request) {
        return service.update(id, request);
    }

    @PostMapping("/{id}/publish")
    public CourseResponse publish(@PathVariable Long id) {
        return service.publish(id);
    }

    @PostMapping("/{id}/archive")
    public CourseResponse archive(@PathVariable Long id) {
        return service.archive(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping("/{id}/modules/{moduleId}/order")
    public CourseResponse order(@PathVariable Long id, @PathVariable Long moduleId, @RequestParam int newOrder) {
        return service.changeModuleOrder(id, moduleId, newOrder);
    }
}