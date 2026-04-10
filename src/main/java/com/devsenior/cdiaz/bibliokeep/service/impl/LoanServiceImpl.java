package com.devsenior.cdiaz.bibliokeep.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.devsenior.cdiaz.bibliokeep.exception.BadRequestException;
import com.devsenior.cdiaz.bibliokeep.exception.ResourceNotFoundException;
import com.devsenior.cdiaz.bibliokeep.mapper.LoanMapper;
import com.devsenior.cdiaz.bibliokeep.model.dto.loan.LoanRequest;
import com.devsenior.cdiaz.bibliokeep.model.dto.loan.LoanResponse;
import com.devsenior.cdiaz.bibliokeep.repository.BookRepository;
import com.devsenior.cdiaz.bibliokeep.repository.LoanRepository;
import com.devsenior.cdiaz.bibliokeep.service.LoanService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final LoanMapper loanMapper;

    @Override
    public LoanResponse createLoan(LoanRequest request, UUID ownerId) {
        var book = bookRepository.findById(request.bookId())
                .filter(b -> b.getOwner().getId().equals(ownerId))
                .orElseThrow(() -> new ResourceNotFoundException("Book not found para el usuario"));

        if (book.getIsLent()) {
            throw new BadRequestException("El libro ya está prestado");
        }

        book.setIsLent(true);
        bookRepository.save(book);

        var loan = loanMapper.toEntity(request);
        loan.setBook(book);
        loan.setReturned(false);

        var saved = loanRepository.save(loan);
        return loanMapper.toResponse(saved);
    }
}
