package com.kawevk.vkurso.module.exceptions;

public class ModuleNotFoundException extends RuntimeException {
    public ModuleNotFoundException(Long id) {
        super("Módulo não encontrado com ID: " + id);
    }
}
