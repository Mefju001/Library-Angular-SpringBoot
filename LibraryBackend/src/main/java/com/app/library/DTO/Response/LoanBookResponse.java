package com.app.library.DTO.Response;

import java.time.LocalDate;

public record LoanBookResponse(
        Integer rentalId,
        Integer userId,
        String username,
        String userFullName,
        String userEmail,
        Integer bookId,
        String bookTitle,
        String bookAuthorName,
        String bookAuthorSurname,
        String bookGenre,
        String bookPublisher,
        LocalDate bookPublicationDate,
        String bookIsbn,
        String bookLanguage,
        Integer bookPages,
        Double bookPrice,
        Double bookOldPrice,
        String rentalStartDate,
        String rentalEndDate,
        String returnRequestDate,
        String status,
        Integer penalty,
        Integer days,
        Long remainingDays,
        Boolean overdue) {
}
