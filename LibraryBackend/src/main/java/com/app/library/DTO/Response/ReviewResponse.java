package com.app.library.DTO.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        String content,
        int rating,
        LocalDate createdAt,
        UserResponse user,
        BookResponse book) {
}
