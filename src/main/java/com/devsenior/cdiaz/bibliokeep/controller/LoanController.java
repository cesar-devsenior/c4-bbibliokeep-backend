package com.devsenior.cdiaz.bibliokeep.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devsenior.cdiaz.bibliokeep.model.dto.loan.LoanRequest;
import com.devsenior.cdiaz.bibliokeep.model.dto.loan.LoanResponse;
import com.devsenior.cdiaz.bibliokeep.service.LoanService;
import com.devsenior.cdiaz.bibliokeep.utils.SecurityUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;
    private final SecurityUtils securityUtils;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanResponse createLoan(@Valid @RequestBody LoanRequest request) {
        var ownerId = securityUtils.getCurrentUserId();
        return loanService.createLoan(request, ownerId);
    }

}
