package com.app.library.EventListener;

import com.app.library.Entity.Rental;
import com.app.library.Entity.RentalStatus;
import com.app.library.Repository.RentalRepository;
import com.app.library.Service.RentalService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RentalEventListener {

    private final RentalService rentalService;

    public RentalEventListener( RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @EventListener
    public void onRentalCreated(RentalCreatedEvent event) {
        Rental rental = event.getRental();
        if (RentalStatus.pending.equals(rental.getStatus())) {
            rentalService.approveLoanBook(rental.getBook().getId(), rental.getUser().getId());
        }
    }
    @EventListener
    public void onRentalReturned(RentalCreatedEvent event) {
        Rental rental = event.getRental();
        if (RentalStatus.return_requested.equals(rental.getStatus())) {
            rentalService.approveReturn(rental.getRentalId());
        }
    }
}
