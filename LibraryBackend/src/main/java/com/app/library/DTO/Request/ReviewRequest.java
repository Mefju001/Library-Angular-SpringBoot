package com.app.library.DTO.Request;

import java.time.LocalDateTime;

public record ReviewRequest(
        String content,
        int rating,
        LocalDateTime createdAt,
        Long userId,
        Integer bookId) {
}
