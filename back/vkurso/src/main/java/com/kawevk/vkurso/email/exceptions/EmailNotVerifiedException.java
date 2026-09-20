package com.kawevk.vkurso.email.exceptions;

public class EmailNotVerifiedException extends RuntimeException {
    public EmailNotVerifiedException(String email) {
        super("Email não verificado: " + email);
    }
}
