package com.kawevk.vkurso.user.dtos;

import com.kawevk.vkurso.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @NotBlank @Size(max = 150)
        String fullName,
        @NotBlank
        @Email @Size(max = 100)
        String email,
        @NotBlank
        String passwordHash,
        @NotBlank @Size(max = 20)
        Role role
) {
}
