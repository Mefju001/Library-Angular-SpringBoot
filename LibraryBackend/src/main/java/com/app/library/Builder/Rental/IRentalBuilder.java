package com.app.library.Builder.Rental;

import com.app.library.Entity.*;
import org.springframework.stereotype.Component;

@Component
public interface IRentalBuilder {
    IRentalBuilder CreateNewRental(Book book, User user,RentalStatus rentalStatus);
    IRentalBuilder WithStartAndEndDate();
    IRentalBuilder WithExtension();
    IRentalBuilder WithPenalty();
    IRentalBuilder WithGenre(Genre genre);
    Rental build();
}
