package com.devsenior.cdiaz.bibliokeep.client;

import com.devsenior.cdiaz.bibliokeep.model.dto.book.BookResponse;

import java.util.List;

public interface GoogleBooksClient {
    List<BookResponse> searchByIsbn(String isbn);

    List<BookResponse> searchByQuery(String query);
}
