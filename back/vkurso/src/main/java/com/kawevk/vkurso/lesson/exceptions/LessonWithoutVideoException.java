package com.kawevk.vkurso.lesson.exceptions;

public class LessonWithoutVideoException extends RuntimeException {
    public LessonWithoutVideoException() {
        super("Esta aula ainda não possui vídeo.");
    }
}
