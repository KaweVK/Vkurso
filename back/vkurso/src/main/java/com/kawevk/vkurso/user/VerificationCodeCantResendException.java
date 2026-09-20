package com.kawevk.vkurso.user;

public class VerificationCodeCantResendException extends RuntimeException {
    public VerificationCodeCantResendException() {
        super("Aguarde antes de solicitar um novo código");
    }
}
