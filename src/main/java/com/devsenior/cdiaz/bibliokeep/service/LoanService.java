package com.devsenior.cdiaz.bibliokeep.service;

import com.devsenior.cdiaz.bibliokeep.model.dto.loan.LoanRequest;
import com.devsenior.cdiaz.bibliokeep.model.dto.loan.LoanResponse;

import java.util.UUID;

public interface LoanService {
    LoanResponse createLoan(LoanRequest request, UUID ownerId);
}
