package com.app.library.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
    private  User user;
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private  Book book;
    @Column(name = "rental_start_date")
    private  LocalDate rentalStartDate;
    @Column(name = "rental_end_date")
    private  LocalDate rentalEndDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RentalStatus status;
    public Rental(Integer rentalId, User userId, Book bookId, LocalDate rentalStartDate, LocalDate rentalEndDate) {
        this.rentalId = rentalId;
        this.user = userId;
        this.book = bookId;
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
    }
    public Rental() {

    }
    public void startLoan()
    {
        this.status= RentalStatus.pending;
        this.rentalStartDate = LocalDate.now();
        this.rentalEndDate =this.rentalStartDate.plusMonths(3);
    }
    public void endLoan()
    {
        this.status = RentalStatus.returned;
    }
    public void extendLoan()
    {
        this.rentalEndDate =this.rentalEndDate.plusMonths(1);
    }
    public boolean isOverdue()
    {
        LocalDate today = LocalDate.now();
        if(this.status == pending && rentalEndDate.isBefore(today))
        {
            this.status = RentalStatus.overdue;
            return true;
        }
        return false;
    }
    public Long getRemainingDays()
    {

        return ChronoUnit.DAYS.between(LocalDate.now(),this.rentalEndDate);
    }
    public void cancelLoan() {
        if (this.status != pending) {
            throw new IllegalStateException("Only active loans can be cancelled.");
        }
        this.status = RentalStatus.cancelled;
    }


    /*

Historia wypożyczeń użytkownika (/users/{id}/history).
 */
}
