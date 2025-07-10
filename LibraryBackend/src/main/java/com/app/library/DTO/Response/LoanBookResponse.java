package com.app.library.DTO.Response;

import java.time.LocalDate;

public record LoanBookResponse(
        Integer rentalId,
        UserResponse userResponse,
        BookResponse bookResponse,
        String rentalStartDate,
        String rentalEndDate,
        String returnRequestDate,
        String status,
        Integer penalty,
        Integer days,
        Long remainingDays,
        Boolean overdue) {
}
