package com.app.library.DTO.Response;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        String content,
        int rating,
        LocalDateTime createdAt,
        UserResponse user,
        BookResponse book) {
}
