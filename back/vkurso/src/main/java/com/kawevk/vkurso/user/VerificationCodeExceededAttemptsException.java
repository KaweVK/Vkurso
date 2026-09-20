package com.kawevk.vkurso.user;

public class VerificationCodeExceededAttemptsException extends RuntimeException {
    public VerificationCodeExceededAttemptsException() {
        super("Número máximo de tentativas excedido");
    }
}
