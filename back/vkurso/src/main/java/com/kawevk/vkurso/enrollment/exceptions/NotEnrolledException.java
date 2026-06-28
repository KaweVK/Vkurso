package com.kawevk.vkurso.enrollment.exceptions;

public class NotEnrolledException extends RuntimeException {
    public NotEnrolledException(Long courseId) {
        super("Aluno não está inscrito no curso: " + courseId);
    }
}