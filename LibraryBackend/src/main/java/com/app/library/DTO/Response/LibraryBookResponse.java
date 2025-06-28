package com.app.library.DTO.Response;

import lombok.Builder;

@Builder
public record LibraryBookResponse(Integer id, BookResponse book, LibraryResponse library) {
}
