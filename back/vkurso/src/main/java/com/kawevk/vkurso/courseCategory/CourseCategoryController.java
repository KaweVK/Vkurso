package com.kawevk.vkurso.courseCategory;

import com.kawevk.vkurso.courseCategory.dtos.CourseCategoryResponse;
import com.kawevk.vkurso.courseCategory.dtos.CreateCourseCategoryRequest;
import com.kawevk.vkurso.courseCategory.dtos.UpdateCourseCategoryRequest;
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
@RequestMapping("/api/course-category")
public class CourseCategoryController {

    private final CourseCategoryService service;

    public CourseCategoryController(CourseCategoryService service) {
        this.service = service;
    }

    @GetMapping
    public Page<CourseCategoryResponse> list(Pageable pageable) {
        return service.list(pageable);
    }

    @GetMapping("/{id}")
    public CourseCategoryResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public ResponseEntity<CourseCategoryResponse> create(@RequestBody @Valid CreateCourseCategoryRequest request, UriComponentsBuilder uriBuilder) {
        CourseCategoryResponse created = service.create(request);
        URI location = uriBuilder.path("/api/course-category/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public CourseCategoryResponse update(@PathVariable Long id, @RequestBody @Valid UpdateCourseCategoryRequest request, @AuthenticationPrincipal User user) {
        return service.update(request, id, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        service.delete(id, user);
    }

}
