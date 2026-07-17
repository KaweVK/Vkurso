package com.kawevk.vkurso.course;

import com.kawevk.vkurso.course.dtos.CourseResponse;
import com.kawevk.vkurso.course.dtos.CreateCourseRequest;
import com.kawevk.vkurso.course.dtos.UpdateCourseRequest;
import com.kawevk.vkurso.user.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/id/{id}")
    public CourseResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/{slug}")
    public CourseResponse getBySlug(@PathVariable String slug) {
        return service.getBySlug(slug);
    }

    @GetMapping("/by-instructor")
    public Page<CourseResponse> listByInstructor(@RequestParam Long instructorId, Pageable pageable) {
        return service.listByInstructor(instructorId, pageable);
    }

    @PostMapping
    public ResponseEntity<CourseResponse> create(@RequestBody @Valid CreateCourseRequest request, UriComponentsBuilder uriBuilder, @AuthenticationPrincipal User user) {
        CourseResponse created = service.create(request, user);
        URI location = uriBuilder.path("/api/courses/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PostMapping("/{id}/categories/{idCategory}")
    public CourseResponse addCategory(@PathVariable Long id, @PathVariable Long idCategory, @AuthenticationPrincipal User user) {
        return service.addCategory(id, idCategory, user);
    }

    @DeleteMapping("/{id}/categories/{idCategory}")
    public CourseResponse removeCategory(@PathVariable Long id, @PathVariable Long idCategory, @AuthenticationPrincipal User user) {
        return service.removeCategory(id, idCategory, user);
    }

    @PutMapping("/{id}")
    public CourseResponse update(@PathVariable Long id, @RequestBody @Valid UpdateCourseRequest request, @AuthenticationPrincipal User user) {
        return service.update(id, request, user);
    }

    @PostMapping("/{id}/publish")
    public CourseResponse publish(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return service.publish(id, user);
    }

    @PostMapping("/{id}/archive")
    public CourseResponse archive(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return service.archive(id, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        service.delete(id, user);
    }

    @PutMapping("/{id}/modules/{moduleId}/order")
    public CourseResponse order(@PathVariable Long id, @PathVariable Long moduleId, @RequestParam int newOrder, @AuthenticationPrincipal User user) {
        return service.changeModuleOrder(id, moduleId, newOrder, user);
    }
}