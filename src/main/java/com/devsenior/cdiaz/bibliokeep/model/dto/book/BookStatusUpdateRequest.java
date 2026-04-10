package com.devsenior.cdiaz.bibliokeep.model.dto.book;

import com.devsenior.cdiaz.bibliokeep.model.entity.BookStatus;
import jakarta.validation.constraints.NotNull;

public record BookStatusUpdateRequest(
        @NotNull BookStatus status
) {
}
