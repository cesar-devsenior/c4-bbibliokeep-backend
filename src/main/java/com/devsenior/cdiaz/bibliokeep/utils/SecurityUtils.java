package com.devsenior.cdiaz.bibliokeep.utils;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.devsenior.cdiaz.bibliokeep.config.security.UserPrincipal;
import com.devsenior.cdiaz.bibliokeep.exception.ResourceNotFoundException;

@Component
public class SecurityUtils {
    
    public UUID getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new ResourceNotFoundException("No authenticated user found");
        }

        var principal = (UserPrincipal) auth.getPrincipal();
        if (principal == null) {
            throw new ResourceNotFoundException("No user principal found in authentication");
        }

        return principal.getId();
    }
}
