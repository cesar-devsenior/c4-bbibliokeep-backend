package com.devsenior.cdiaz.bibliokeep.repository;

import com.devsenior.cdiaz.bibliokeep.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByBookIdAndReturnedFalse(Long bookId);

    List<Loan> findByDueDateBeforeAndReturnedFalse(LocalDate date);
}
