package com.app.library.DTO.Response;

import lombok.Builder;

@Builder
public record LibraryBookResponse(
        BookResponse book,
        LibraryResponse library,
        int Stock) {
}
