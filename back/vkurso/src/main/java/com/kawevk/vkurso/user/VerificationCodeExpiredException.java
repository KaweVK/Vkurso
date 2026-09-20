package com.kawevk.vkurso.user;

public class VerificationCodeExpiredException extends RuntimeException {
    public VerificationCodeExpiredException() {
        super("Códido expirado");
    }
}
