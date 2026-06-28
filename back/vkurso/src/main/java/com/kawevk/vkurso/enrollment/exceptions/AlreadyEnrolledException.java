package com.kawevk.vkurso.enrollment.exceptions;

public class AlreadyEnrolledException extends RuntimeException {
    public AlreadyEnrolledException(Long courseId) {
        super("Aluno já inscrito no curso: " + courseId);
    }
}