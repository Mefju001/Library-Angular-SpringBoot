package com.app.library.Entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Rental")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id")
    private Integer rentalId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    @Column(name = "rental_start_date")
    private LocalDate rentalStartDate;
    @Column(name = "rental_end_date")
    private LocalDate rentalEndDate;
    @Column(name = "return_request_date")
    private LocalDate returnRequestDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RentalStatus status;
    @Column(name = "penalty")
    private Double penalty;
    @Column(name = "extensionCount")
    private Integer extensionCount;

    public Rental(Integer rentalId, User userId, Book bookId, LocalDate rentalStartDate, LocalDate rentalEndDate) {
        this.rentalId = rentalId;
        this.user = userId;
        this.book = bookId;
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
    }
    public Rental(Book book, User user, RentalStatus status) {
        this.book = book;
        this.user = user;
        this.status = status;
    }
    public Rental() {

    }
    public void approveLoan() {
        if (!RentalStatus.pending.equals(this.getStatus())) {
            throw new IllegalStateException("Wypożyczenie musi mieć status 'pending' do zatwierdzenia.");
        }
        this.setExtensionCount(0);
        this.setRentalEndDate(LocalDate.now().plusMonths(3));
        this.setRentalStartDate(LocalDate.now());
        this.setStatus(RentalStatus.loaned);
        this.setPenalty(0.0);
    }
    public void approveReturn(double penaltyPrice) {
        if (!RentalStatus.return_requested.equals(this.getStatus())) {
            throw new IllegalStateException("Should have status 'return_requested' to approve.");
        }
        this.setStatus(RentalStatus.returned);
        this.setRentalEndDate(LocalDate.now());
        this.setPenalty(penaltyPrice);
    }
    public void approveExtendLoan(double penaltyPrice) {

    }
    public Integer getExtensionCount() {
        return extensionCount;
    }

    public void setExtensionCount(Integer extensionCount) {
        this.extensionCount = extensionCount;
    }

    public Integer getRentalId() {
        return rentalId;
    }

    public void setRentalId(Integer rentalId) {
        this.rentalId = rentalId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getRentalStartDate() {
        return rentalStartDate;
    }

    public void setRentalStartDate(LocalDate rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    public LocalDate getRentalEndDate() {
        return rentalEndDate;
    }

    public void setRentalEndDate(LocalDate rentalEndDate) {
        this.rentalEndDate = rentalEndDate;
    }

    public LocalDate getReturnRequestDate() {
        return returnRequestDate;
    }

    public void setReturnRequestDate(LocalDate returnRequestDate) {
        this.returnRequestDate = returnRequestDate;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }

    public Double getPenalty() {
        return penalty;
    }

    public void setPenalty(Double penalty) {
        this.penalty = penalty;
    }
/*

Historia wypożyczeń użytkownika (/users/{id}/history).
 */
}
