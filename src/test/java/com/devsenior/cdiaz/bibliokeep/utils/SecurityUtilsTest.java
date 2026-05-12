package com.devsenior.cdiaz.bibliokeep.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.devsenior.cdiaz.bibliokeep.config.security.UserPrincipal;
import com.devsenior.cdiaz.bibliokeep.exception.ResourceNotFoundException;
import com.devsenior.cdiaz.bibliokeep.model.entity.User;

class SecurityUtilsTest {

    private SecurityUtils securityUtils;

    @BeforeEach
    void generateToken() {
        this.securityUtils = new SecurityUtils();
    }

    @Test
    void shouldReturnCurrentUserId_whenUserIsAuthenticated() {
        var userPrincipal = new UserPrincipal(User.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                .email("testuser@test.com")
                .password("testpassword")
                .build());
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(
                userPrincipal,
                null,
                userPrincipal.getAuthorities()));
        var expected = UUID.fromString("00000000-0000-0000-0000-000000000000");

        UUID actual = securityUtils.getCurrentUserId();

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowException_whenNoAuthenticatedUser() {
        SecurityContextHolder.getContext().setAuthentication(null);

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> securityUtils.getCurrentUserId());

        assertEquals("No authenticated user found", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenNoUserPrincipalInAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        null,
                        null,
                        null));

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> securityUtils.getCurrentUserId());

        assertEquals("No user principal found in authentication", exception.getMessage());
    }
}
