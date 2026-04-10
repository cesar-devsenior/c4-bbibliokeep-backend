package com.devsenior.cdiaz.bibliokeep.model.dto.stats;

public record DashboardStatsResponse(
        Long totalBooks,
        Long desiredBooks,
        Long purchasedBooks,
        Long readingBooks,
        Long readBooks,
        Long abandonedBooks,
        Long lentBooks,
        Long overdueLoans,
        Integer annualGoal,
        Long booksRemainingToGoal
) {
}
