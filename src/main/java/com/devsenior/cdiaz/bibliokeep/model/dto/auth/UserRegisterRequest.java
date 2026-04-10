package com.devsenior.cdiaz.bibliokeep.model.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Set;

public record UserRegisterRequest(
        @Email String email,
        @NotBlank String password,
        @NotEmpty Set<String> preferences,
        @PositiveOrZero Integer annualGoal
) {
}
