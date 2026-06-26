package com.kawevk.vkurso.course.exceptions;

public class DuplicateSlugException extends RuntimeException {
    public DuplicateSlugException(String slug) {
        super("Já existe um curso com o slug: " + slug);
    }
}
