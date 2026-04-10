package com.devsenior.cdiaz.bibliokeep.service.impl;

import com.devsenior.cdiaz.bibliokeep.config.security.UserPrincipal;
import com.devsenior.cdiaz.bibliokeep.exception.ResourceNotFoundException;
import com.devsenior.cdiaz.bibliokeep.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
        return new UserPrincipal(user);
    }

    public UserPrincipal loadUserById(java.util.UUID userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + userId));
        return new UserPrincipal(user);
    }
}
