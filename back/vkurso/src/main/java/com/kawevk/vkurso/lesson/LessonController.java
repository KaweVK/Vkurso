package com.kawevk.vkurso.lesson;

import com.kawevk.vkurso.lesson.dtos.CreateLessonRequest;
import com.kawevk.vkurso.lesson.dtos.LessonResponse;
import com.kawevk.vkurso.lesson.dtos.UpdateLessonRequest;
import com.kawevk.vkurso.lesson.dtos.VideoUrlResponse;
import com.kawevk.vkurso.user.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/courses/{courseId}/modules/{moduleId}/lessons")
public class LessonController {

    private final LessonService service;

    public LessonController(LessonService service) {
        this.service = service;
    }

    @GetMapping
    public Page<LessonResponse> list(Pageable pageable) {
        return service.list(pageable);
    }

    @GetMapping("/{id}")
    public LessonResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<LessonResponse> create(@PathVariable Long courseId, @PathVariable Long moduleId, @RequestBody @Valid CreateLessonRequest req, UriComponentsBuilder uri) {

        LessonResponse created = service.create(moduleId, req);
        URI location = uri.path("api/courses/{courseId}/modules/{moduleId}/lessons/").buildAndExpand(courseId, moduleId, created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PostMapping(value = "/{id}/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public LessonResponse uploadVideo(@PathVariable Long id,
                                      @RequestParam("file") MultipartFile file) {
        return service.attachVideo(id, file);
    }

    @PutMapping("/{id}")
    public LessonResponse update(@PathVariable Long id, @RequestBody @Valid UpdateLessonRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}/video-url")
    public VideoUrlResponse videoUrl(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return service.videoUrl(id, user.getId());
    }

}
