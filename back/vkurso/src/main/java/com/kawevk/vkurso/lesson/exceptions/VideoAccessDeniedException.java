package com.kawevk.vkurso.lesson.exceptions;

public class VideoAccessDeniedException extends RuntimeException {
    public VideoAccessDeniedException(Long lessonId) {
        super("Você não tem acesso ao vídeo desta aula: " + lessonId);
    }
}