package com.kawevk.vkurso.email.dtos;

public record VerifyEmailRequest(
        String email,
        String code
) {}