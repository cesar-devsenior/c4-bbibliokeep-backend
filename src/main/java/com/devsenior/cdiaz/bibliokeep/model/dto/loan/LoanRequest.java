package com.devsenior.cdiaz.bibliokeep.model.dto.loan;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record LoanRequest(
        @NotNull Long bookId,
        @NotBlank String contactName,
        @NotNull @FutureOrPresent LocalDate loanDate,
        @NotNull @Future LocalDate dueDate
) {
}
