package com.kawevk.vkurso.email.exceptions;

public class InvalidVerificationCodeException extends RuntimeException {
    public InvalidVerificationCodeException(String code) {
        super("Códido expirado: " + code);
    }
}
