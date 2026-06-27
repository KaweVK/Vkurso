package com.kawevk.vkurso.user.exceptions;

public class UserNotCreatedException extends RuntimeException {
    public UserNotCreatedException(String email) {
        super("Usuário não encontrado: " + email);
    }
}
