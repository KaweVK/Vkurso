package com.kawevk.vkurso.course.exceptions;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(Long id) {
        super("Curso não encontrado com id: " + id);
    }

    public CourseNotFoundException() {
        super("Nenhum curso encontrado.");
    }
}
