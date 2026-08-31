package com.kawevk.vkurso.learningProgress.exceptions;

public class ProgressNotFoundException extends RuntimeException {
    public ProgressNotFoundException(Long id) {
        super("Progress not found with id: " + id);
    }
}
