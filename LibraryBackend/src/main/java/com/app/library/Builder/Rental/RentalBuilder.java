package com.app.library.Builder.Rental;

import com.app.library.Builder.Book.IBookBuilder;
import com.app.library.Entity.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class RentalBuilder implements IRentalBuilder {
    private Rental rental;
    @Override
    public IRentalBuilder CreateNewRental(Book book, User user, RentalStatus rentalStatus) {
        rental = new Rental(book,user,rentalStatus);
        return this;
    }

    @Override
    public IRentalBuilder WithStartAndEndDate() {
        rental.setRentalStartDate(LocalDate.now());
        rental.setRentalEndDate(LocalDate.now().plusMonths(3));
        return this;
    }

    @Override
    public IRentalBuilder WithExtension() {
        rental.setExtensionCount(0);
        return this;
    }

    @Override
    public IRentalBuilder WithPenalty() {
        rental.setPenalty(0.0);
        return this;
    }

    @Override
    public IRentalBuilder WithGenre(Genre genre) {
        return null;
    }

    @Override
    public Rental build() {
        return rental;
    }
}
