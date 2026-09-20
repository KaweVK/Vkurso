package com.kawevk.vkurso.user.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Usuário não encontrado com ID: " + id);
    }

    public UserNotFoundException(String email) {
        super("Usuário não encontrado com email: " + email);
    }
}
