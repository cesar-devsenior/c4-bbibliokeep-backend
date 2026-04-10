package com.devsenior.cdiaz.bibliokeep.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devsenior.cdiaz.bibliokeep.config.security.JwtTokenProvider;
import com.devsenior.cdiaz.bibliokeep.exception.BadRequestException;
import com.devsenior.cdiaz.bibliokeep.mapper.UserMapper;
import com.devsenior.cdiaz.bibliokeep.model.dto.auth.AuthResponse;
import com.devsenior.cdiaz.bibliokeep.model.dto.auth.UserLoginRequest;
import com.devsenior.cdiaz.bibliokeep.model.dto.auth.UserRegisterRequest;
import com.devsenior.cdiaz.bibliokeep.model.entity.User;
import com.devsenior.cdiaz.bibliokeep.repository.UserRepository;
import com.devsenior.cdiaz.bibliokeep.service.AuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    @Override
    public AuthResponse register(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email ya está en uso");
        }

        var user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setPreferences(request.preferences());
        user.setAnnualGoal(request.annualGoal() != null ? request.annualGoal() : 12);

        var saved = userRepository.save(user);

        var auth = new UsernamePasswordAuthenticationToken(saved.getEmail(), request.password());
        var authentication = authenticationManager.authenticate(auth);
        var token = jwtTokenProvider.generateToken(authentication);

        return userMapper.toAuthResponse(saved, token, "");
    }

    @Override
    public AuthResponse login(UserLoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadRequestException("Usuario o contraseña inválidos"));

        var token = jwtTokenProvider.generateToken(authentication);
        return userMapper.toAuthResponse(user, token, "");
    }
}
