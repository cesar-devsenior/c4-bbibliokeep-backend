package com.devsenior.cdiaz.bibliokeep.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.devsenior.cdiaz.bibliokeep.exception.BadRequestException;
import com.devsenior.cdiaz.bibliokeep.exception.ResourceNotFoundException;
import com.devsenior.cdiaz.bibliokeep.mapper.LoanMapper;
import com.devsenior.cdiaz.bibliokeep.model.dto.loan.LoanRequest;
import com.devsenior.cdiaz.bibliokeep.model.dto.loan.LoanResponse;
import com.devsenior.cdiaz.bibliokeep.model.entity.Book;
import com.devsenior.cdiaz.bibliokeep.model.entity.Loan;
import com.devsenior.cdiaz.bibliokeep.model.entity.User;
import com.devsenior.cdiaz.bibliokeep.repository.BookRepository;
import com.devsenior.cdiaz.bibliokeep.repository.LoanRepository;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LoanMapper loanMapper;

    @InjectMocks
    private LoanServiceImpl service;

    @Test
    void shouldThrowResourceNotFound_WhenBookDoesNotExist() {
        var request = new LoanRequest(1L, "Ana", LocalDate.now(), LocalDate.now().plusDays(7));
        var ownerId = UUID.randomUUID();

        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createLoan(request, ownerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Book not found");
    }

    @Test
    void shouldThrowResourceNotFound_WhenBookNotOwnedByUser() {
        var request = new LoanRequest(2L, "Ana", LocalDate.now(), LocalDate.now().plusDays(7));
        var ownerId = UUID.randomUUID();

        var book = new Book();
        var otherOwner = new User();
        otherOwner.setId(UUID.randomUUID());
        book.setOwner(otherOwner);

        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> service.createLoan(request, ownerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Book not found");
    }

    @Test
    void shouldThrowBadRequest_WhenBookAlreadyLent() {
        var request = new LoanRequest(3L, "Luis", LocalDate.now(), LocalDate.now().plusDays(7));
        var ownerId = UUID.randomUUID();

        var book = new Book();
        var owner = new User();
        owner.setId(ownerId);
        book.setOwner(owner);
        book.setIsLent(true);

        when(bookRepository.findById(3L)).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> service.createLoan(request, ownerId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("prestado");
    }

    @Test
    void shouldCreateLoan_WhenBookAvailable() {
        var request = new LoanRequest(4L, "Paco", LocalDate.now(), LocalDate.now().plusDays(7));
        var ownerId = UUID.randomUUID();

        var book = new Book();
        var owner = new User();
        owner.setId(ownerId);
        book.setOwner(owner);
        book.setIsLent(false);
        book.setId(4L);

        when(bookRepository.findById(4L)).thenReturn(Optional.of(book));

        var loanEntity = new Loan();
        when(loanMapper.toEntity(request)).thenReturn(loanEntity);

        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> {
            var l = invocation.getArgument(0, Loan.class);
            l.setId(99L);
            return l;
        });

        when(loanMapper.toResponse(any(Loan.class))).thenReturn(new LoanResponse(99L, 4L, "Paco", request.loanDate(), request.dueDate(), false));

        var response = service.createLoan(request, ownerId);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(99L);
        assertThat(book.getIsLent()).isTrue();

        verify(bookRepository).save(book);
        verify(loanRepository).save(loanEntity);
    }
}
