package com.kawevk.vkurso.email.exceptions;

public class EmailAlreadyVerifiedException extends RuntimeException {
    public EmailAlreadyVerifiedException(String email) {
        super("Email já verificado: " + email);
    }
}