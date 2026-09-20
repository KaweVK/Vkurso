package com.kawevk.vkurso.email.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Email já registrado: " + email);
    }
}