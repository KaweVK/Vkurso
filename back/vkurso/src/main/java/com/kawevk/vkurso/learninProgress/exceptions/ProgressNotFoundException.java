package com.kawevk.vkurso.learninProgress.exceptions;

public class ProgressNotFoundException extends RuntimeException {
    public ProgressNotFoundException(Long id) {
        super("Progress not found with id: " + id);
    }
}
