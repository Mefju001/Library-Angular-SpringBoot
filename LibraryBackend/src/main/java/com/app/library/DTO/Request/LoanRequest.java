package com.app.library.DTO.Request;

import jakarta.validation.constraints.NotNull;

public record LoanRequest(
        @NotNull Long userId,
        @NotNull Integer bookId
) {
}

