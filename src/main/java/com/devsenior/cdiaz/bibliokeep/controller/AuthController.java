package com.devsenior.cdiaz.bibliokeep.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devsenior.cdiaz.bibliokeep.model.dto.auth.AuthResponse;
import com.devsenior.cdiaz.bibliokeep.model.dto.auth.UserLoginRequest;
import com.devsenior.cdiaz.bibliokeep.model.dto.auth.UserRegisterRequest;
import com.devsenior.cdiaz.bibliokeep.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody UserRegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@Valid @RequestBody UserLoginRequest request) {
        return authService.login(request);
    }
}
