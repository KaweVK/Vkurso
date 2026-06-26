package com.kawevk.vkurso.course.dtos;

import com.kawevk.vkurso.course.CourseLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record UpdateCourseRequest(
        @NotBlank @Size(max = 150)
        String title,
        String description,
        @NotNull
        CourseLevel level
) {
}
