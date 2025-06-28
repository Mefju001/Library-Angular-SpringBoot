package com.app.library.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.app.library.Entity.RentalStatus.loaned;
import static com.app.library.Entity.RentalStatus.pending;

@Data
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

    public Rental(Integer rentalId, User userId, Book bookId, LocalDate rentalStartDate, LocalDate rentalEndDate) {
        this.rentalId = rentalId;
        this.user = userId;
        this.book = bookId;
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
    }

    public Rental() {

    }

    public void requestStartLoan() {
        this.status = pending;

    }

    public void startLoan() {
        this.status = loaned;
        this.rentalStartDate = LocalDate.now();
        this.rentalEndDate = this.rentalStartDate.plusMonths(3);
    }

    public void requestEndLoan() {
        if (this.status != loaned) {
            throw new IllegalStateException("Return can only be requested for loaned books");
        }
        this.status = RentalStatus.return_requested;
        this.returnRequestDate = LocalDate.now();

    }

    public void endLoan() {
        this.status = RentalStatus.returned;
    }

    public void extendLoan() {
        this.status = RentalStatus.extend;
        this.rentalEndDate = this.rentalEndDate.plusMonths(1);
    }

    public boolean isOverdue() {
        LocalDate today = LocalDate.now();
        if (this.status == loaned && rentalEndDate.isBefore(today)) {
            this.status = RentalStatus.overdue;
            return true;
        }
        return false;
    }

    public LoanDeadlineInfo getRemainingDays() {
        long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), this.rentalEndDate);
        boolean isOverdue = daysBetween < 0;
        return new LoanDeadlineInfo(Math.abs(daysBetween), isOverdue);
    }

    public Integer getDays() {
        if (this.returnRequestDate != null && this.rentalEndDate != null) {
            // Liczymy dni od daty zakończenia wypożyczenia do daty zwrotu
            return Math.toIntExact(ChronoUnit.DAYS.between(this.rentalEndDate, this.returnRequestDate));
        }
        return 0; // Jeśli nie ma dat, zwracamy 0 dni (lub można rzucić wyjątek)
    }

    public void cancelLoan() {
        if (this.status != pending) {
            throw new IllegalStateException("Only active loans can be cancelled.");
        }
        this.status = RentalStatus.cancelled;
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
