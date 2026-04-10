package com.devsenior.cdiaz.bibliokeep.service;

import com.devsenior.cdiaz.bibliokeep.model.dto.book.BookRequest;
import com.devsenior.cdiaz.bibliokeep.model.dto.book.BookResponse;
import com.devsenior.cdiaz.bibliokeep.model.entity.BookStatus;

import java.util.List;
import java.util.UUID;

public interface BookService {
    BookResponse createBook(BookRequest request, UUID ownerId);

    BookResponse updateBookStatus(Long id, BookStatus status, UUID ownerId);

    List<BookResponse> searchBooks(String query, UUID ownerId);
}
