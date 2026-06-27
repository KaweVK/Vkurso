package com.kawevk.vkurso.lesson.exceptions;

public class LessonNotFoundException extends RuntimeException {
    public LessonNotFoundException(Long id) {
        super("Aula não encontrada com id: " + id);
    }
}