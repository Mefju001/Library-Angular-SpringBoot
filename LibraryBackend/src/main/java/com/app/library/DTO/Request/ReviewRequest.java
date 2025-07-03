package com.app.library.DTO.Request;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReviewRequest(
        String content,
        int rating,
        LocalDate createdAt,
        Long userId,
        Integer bookId) {
}
