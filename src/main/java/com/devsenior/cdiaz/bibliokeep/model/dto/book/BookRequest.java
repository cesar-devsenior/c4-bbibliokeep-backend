package com.devsenior.cdiaz.bibliokeep.model.dto.book;

import com.devsenior.cdiaz.bibliokeep.model.entity.BookStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Max;

import java.util.List;

public record BookRequest(
        @NotBlank String isbn,
        @NotBlank String title,
        @NotEmpty List<String> authors,
        String description,
        String thumbnail,
        BookStatus status,
        @Positive @Max(value = 10) Integer rating
) {
}
