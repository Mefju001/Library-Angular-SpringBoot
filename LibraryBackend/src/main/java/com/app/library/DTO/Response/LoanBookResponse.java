package com.app.library.DTO.Response;

import java.time.LocalDate;

public class LoanBookResponse {
    private int rentalId;
    private int userId;
    private String username;
    private String userFullName;
    private String userEmail;

    private int bookId;
    private String bookTitle;
    private String bookAuthorName;
    private String bookAuthorSurname;
    private String bookGenre;
    private String bookPublisher;
    private LocalDate bookPublicationDate;
    private String bookIsbn;
    private String bookLanguage;
    private int bookPages;
    private double bookPrice;
    private double bookOldPrice;

    private String rentalStartDate;  // Data wypożyczenia
    private String rentalEndDate;    // Data zwrotu
    private String returnRequestDate; // Data zgłoszenia zwrotu
    private String status;           // Status wypożyczenia ("pending", "returned", etc.)
    private int penalty;             // Kara za opóźniony zwrot
    private int days;                // Liczba dni wypożyczenia
    private int remainingDays;       // Liczba dni pozostałych do zwrotu
    private boolean overdue;         // Czy książka jest opóźniona

    // Gettery i settery
    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthorName() {
        return bookAuthorName;
    }

    public void setBookAuthorName(String bookAuthorName) {
        this.bookAuthorName = bookAuthorName;
    }

    public String getBookAuthorSurname() {
        return bookAuthorSurname;
    }

    public void setBookAuthorSurname(String bookAuthorSurname) {
        this.bookAuthorSurname = bookAuthorSurname;
    }

    public String getBookGenre() {
        return bookGenre;
    }

    public void setBookGenre(String bookGenre) {
        this.bookGenre = bookGenre;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    public LocalDate getBookPublicationDate() {
        return bookPublicationDate;
    }

    public void setBookPublicationDate(LocalDate bookPublicationDate) {
        this.bookPublicationDate = bookPublicationDate;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public String getBookLanguage() {
        return bookLanguage;
    }

    public void setBookLanguage(String bookLanguage) {
        this.bookLanguage = bookLanguage;
    }

    public int getBookPages() {
        return bookPages;
    }

    public void setBookPages(int bookPages) {
        this.bookPages = bookPages;
    }

    public double getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(double bookPrice) {
        this.bookPrice = bookPrice;
    }

    public double getBookOldPrice() {
        return bookOldPrice;
    }

    public void setBookOldPrice(double bookOldPrice) {
        this.bookOldPrice = bookOldPrice;
    }

    public String getRentalStartDate() {
        return rentalStartDate;
    }

    public void setRentalStartDate(String rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    public String getRentalEndDate() {
        return rentalEndDate;
    }

    public void setRentalEndDate(String rentalEndDate) {
        this.rentalEndDate = rentalEndDate;
    }

    public String getReturnRequestDate() {
        return returnRequestDate;
    }

    public void setReturnRequestDate(String returnRequestDate) {
        this.returnRequestDate = returnRequestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(int remainingDays) {
        this.remainingDays = remainingDays;
    }

    public boolean isOverdue() {
        return overdue;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }
}
