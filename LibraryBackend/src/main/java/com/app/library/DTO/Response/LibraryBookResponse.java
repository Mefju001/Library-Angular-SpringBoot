package com.app.library.DTO.Response;

public record LibraryBookResponse(
        Integer id,
        BookResponse book,
        LibraryResponse library,
        int Stock) {
}
