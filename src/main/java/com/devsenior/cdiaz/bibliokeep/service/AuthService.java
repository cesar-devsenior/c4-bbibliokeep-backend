package com.devsenior.cdiaz.bibliokeep.service;

import com.devsenior.cdiaz.bibliokeep.model.dto.auth.AuthResponse;
import com.devsenior.cdiaz.bibliokeep.model.dto.auth.UserLoginRequest;
import com.devsenior.cdiaz.bibliokeep.model.dto.auth.UserRegisterRequest;

public interface AuthService {
    AuthResponse register(UserRegisterRequest request);

    AuthResponse login(UserLoginRequest request);
}
