package com.app.library.DTO.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReviewResponse(
        String content,
        int rating,
        LocalDate createdAt,
        UserResponse user,
        BookResponse book) {
}
