package com.devsenior.cdiaz.bibliokeep.model.dto.book;

import com.devsenior.cdiaz.bibliokeep.model.entity.BookStatus;

import java.util.List;

public record BookResponse(
        Long id,
        String isbn,
        String title,
        List<String> authors,
        String description,
        String thumbnail,
        BookStatus status,
        Integer rating,
        Boolean isLent
) {
}
