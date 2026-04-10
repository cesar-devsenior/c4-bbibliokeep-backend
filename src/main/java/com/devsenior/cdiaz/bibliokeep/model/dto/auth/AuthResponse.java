package com.devsenior.cdiaz.bibliokeep.model.dto.auth;

import java.util.UUID;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UUID userId,
        String email
) {
}
