package com.devsenior.cdiaz.bibliokeep.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devsenior.cdiaz.bibliokeep.config.security.UserPrincipal;
import com.devsenior.cdiaz.bibliokeep.model.dto.book.BookRequest;
import com.devsenior.cdiaz.bibliokeep.model.dto.book.BookResponse;
import com.devsenior.cdiaz.bibliokeep.model.dto.book.BookStatusUpdateRequest;
import com.devsenior.cdiaz.bibliokeep.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<BookResponse> searchBooks(@RequestParam(name = "q", required = false) String query) {
        var ownerId = getCurrentUserId();
        return bookService.searchBooks(query == null ? "" : query, ownerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(@Valid @RequestBody BookRequest request) {
        var ownerId = getCurrentUserId();
        return bookService.createBook(request, ownerId);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public BookResponse updateStatus(@PathVariable("id") Long id,
                                                     @Valid @RequestBody BookStatusUpdateRequest request) {
        var ownerId = getCurrentUserId();
        return bookService.updateBookStatus(id, request.status(), ownerId);
    }

    private UUID getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var principal = (UserPrincipal) auth.getPrincipal();
        return principal.getId();
    }
}
