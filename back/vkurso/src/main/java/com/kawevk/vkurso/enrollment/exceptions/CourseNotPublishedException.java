package com.kawevk.vkurso.enrollment.exceptions;

public class CourseNotPublishedException extends RuntimeException {
    public CourseNotPublishedException(Long courseId) {
        super("Não é possível se inscrever em um curso que não está publicado: " + courseId);
    }
}