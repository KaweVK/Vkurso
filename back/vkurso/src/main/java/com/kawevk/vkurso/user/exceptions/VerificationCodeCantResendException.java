package com.kawevk.vkurso.user.exceptions;

public class VerificationCodeCantResendException extends RuntimeException {
    public VerificationCodeCantResendException() {
        super("Aguarde antes de solicitar um novo código");
    }
}
