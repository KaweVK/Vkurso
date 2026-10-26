package com.kawevk.vkurso.user.exceptions;

public class VerificationCodeExpiredException extends RuntimeException {
    public VerificationCodeExpiredException() {
        super("Códido expirado");
    }
}
