package com.app.library.DTO.Request;

import jakarta.validation.Valid;

public record LibraryBookRequest(
        @Valid BookRequest book,
        @Valid LibraryRequest library) {
}
