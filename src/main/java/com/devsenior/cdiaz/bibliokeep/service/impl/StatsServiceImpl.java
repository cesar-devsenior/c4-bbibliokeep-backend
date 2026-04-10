package com.devsenior.cdiaz.bibliokeep.service.impl;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.devsenior.cdiaz.bibliokeep.model.dto.stats.DashboardStatsResponse;
import com.devsenior.cdiaz.bibliokeep.model.entity.Book;
import com.devsenior.cdiaz.bibliokeep.model.entity.BookStatus;
import com.devsenior.cdiaz.bibliokeep.repository.BookRepository;
import com.devsenior.cdiaz.bibliokeep.repository.LoanRepository;
import com.devsenior.cdiaz.bibliokeep.repository.UserRepository;
import com.devsenior.cdiaz.bibliokeep.service.StatsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StatsServiceImpl implements StatsService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    @Override
    public DashboardStatsResponse getDashboardStats(UUID ownerId) {
        var user = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        var books = bookRepository.findByOwnerId(ownerId);
        var total = (long) books.size();
        var desired = books.stream().filter(b -> b.getStatus() == BookStatus.DESEADO).count();
        var purchased = books.stream().filter(b -> b.getStatus() == BookStatus.COMPRADO).count();
        var reading = books.stream().filter(b -> b.getStatus() == BookStatus.LEYENDO).count();
        var read = books.stream().filter(b -> b.getStatus() == BookStatus.LEIDO).count();
        var abandoned = books.stream().filter(b -> b.getStatus() == BookStatus.ABANDONADO).count();
        var lent = books.stream().filter(Book::getIsLent).count();

        var overdue = loanRepository.findByDueDateBeforeAndReturnedFalse(LocalDate.now()).stream()
                .filter(loan -> loan.getBook().getOwner().getId().equals(ownerId))
                .count();

        var remaining = Math.max(0L, user.getAnnualGoal() - read);

        return new DashboardStatsResponse(
                total,
                desired,
                purchased,
                reading,
                read,
                abandoned,
                lent,
                overdue,
                user.getAnnualGoal(),
                remaining
        );
    }
}
