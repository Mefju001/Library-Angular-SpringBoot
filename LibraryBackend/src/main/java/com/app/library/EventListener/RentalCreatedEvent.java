package com.app.library.EventListener;

import com.app.library.Entity.Rental;

public class RentalCreatedEvent {
    private final Rental rental;

    public RentalCreatedEvent(Rental rental) {
        this.rental = rental;
    }

    public Rental getRental() {
        return rental;
    }
}
