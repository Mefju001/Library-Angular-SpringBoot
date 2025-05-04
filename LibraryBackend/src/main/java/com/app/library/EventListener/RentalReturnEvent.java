package com.app.library.EventListener;

import com.app.library.Entity.Rental;

public class RentalReturnEvent {
    private final Rental rental;

    public RentalReturnEvent(Rental rental) {
        this.rental = rental;
    }

    public Rental getRental() {
        return rental;
    }
}
