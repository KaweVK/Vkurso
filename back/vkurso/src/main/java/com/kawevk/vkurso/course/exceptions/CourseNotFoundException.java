package com.kawevk.vkurso.course.exceptions;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(Long id) {
        super("Curso não encontrado com id: " + id);
    }

    public CourseNotFoundException(String slug) {
        super("Curso não encontrado com slug: " + slug);
    }

    public CourseNotFoundException() {
        super("Nenhum curso encontrado.");
    }
}
