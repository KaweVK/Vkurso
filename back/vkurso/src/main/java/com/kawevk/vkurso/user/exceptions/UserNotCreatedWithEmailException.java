package com.kawevk.vkurso.user.exceptions;

public class UserNotCreatedWithEmailException extends RuntimeException {
    public UserNotCreatedWithEmailException(String email) {
        super("Usuário não encontrado com este email: " + email);
    }
}
