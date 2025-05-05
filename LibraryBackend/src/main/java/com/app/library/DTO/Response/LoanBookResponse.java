package com.app.library.DTO.Response;

import lombok.Getter;

import java.time.LocalDate;
@Getter
public class LoanBookResponse {
    private final Integer rentalId;
    private final Integer userId;
    private final String username;
    private final String userFullName;
    private final String userEmail;

    private final Integer bookId;
    private final String bookTitle;
    private final String bookAuthorName;
    private final String bookAuthorSurname;
    private final String bookGenre;
    private final String bookPublisher;
    private final LocalDate bookPublicationDate;
    private final String bookIsbn;
    private final String bookLanguage;
    private final Integer  bookPages;
    private final Double bookPrice;
    private final Double bookOldPrice;

    private final String rentalStartDate;
    private final String rentalEndDate;
    private final String returnRequestDate;
    private final String status;
    private final Integer penalty;
    private final Long days;
    private final Long remainingDays;
    private final Boolean overdue;

    public LoanBookResponse(int rentalId, int userId, String username, String userFullName, String userEmail, int bookId, String bookTitle, String bookAuthorName, String bookAuthorSurname, String bookGenre, String bookPublisher, LocalDate bookPublicationDate, String bookIsbn, String bookLanguage, int bookPages, double bookPrice, double bookOldPrice, String rentalStartDate, String rentalEndDate, String returnRequestDate, String status, int penalty, long days, long remainingDays, boolean overdue) {
        this.rentalId = rentalId;
        this.userId = userId;
        this.username = username;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthorName = bookAuthorName;
        this.bookAuthorSurname = bookAuthorSurname;
        this.bookGenre = bookGenre;
        this.bookPublisher = bookPublisher;
        this.bookPublicationDate = bookPublicationDate;
        this.bookIsbn = bookIsbn;
        this.bookLanguage = bookLanguage;
        this.bookPages = bookPages;
        this.bookPrice = bookPrice;
        this.bookOldPrice = bookOldPrice;
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
        this.returnRequestDate = returnRequestDate;
        this.status = status;
        this.penalty = penalty;
        this.days = days;
        this.remainingDays = remainingDays;
        this.overdue = overdue;
    }
}
