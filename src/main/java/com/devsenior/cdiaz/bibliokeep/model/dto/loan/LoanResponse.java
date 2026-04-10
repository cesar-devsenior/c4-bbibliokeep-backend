package com.devsenior.cdiaz.bibliokeep.model.dto.loan;

import java.time.LocalDate;

public record LoanResponse(
        Long id,
        Long bookId,
        String contactName,
        LocalDate loanDate,
        LocalDate dueDate,
        Boolean returned
) {
}
