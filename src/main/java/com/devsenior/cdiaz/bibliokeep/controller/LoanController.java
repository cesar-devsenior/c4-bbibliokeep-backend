package com.devsenior.cdiaz.bibliokeep.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsenior.cdiaz.bibliokeep.config.security.UserPrincipal;
import com.devsenior.cdiaz.bibliokeep.model.dto.loan.LoanRequest;
import com.devsenior.cdiaz.bibliokeep.model.dto.loan.LoanResponse;
import com.devsenior.cdiaz.bibliokeep.service.LoanService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanResponse createLoan(@Valid @RequestBody LoanRequest request) {
        var ownerId = getCurrentUserId();
        return loanService.createLoan(request, ownerId);
    }

    private UUID getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var principal = (UserPrincipal) auth.getPrincipal();
        return principal.getId();
    }
}
