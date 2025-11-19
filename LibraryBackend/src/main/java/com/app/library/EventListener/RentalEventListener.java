package com.app.library.EventListener;

import com.app.library.Entity.Rental;
import com.app.library.Entity.RentalStatus;
import com.app.library.Facade.Rental.RequestRent.RentalProcessingFacade;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RentalEventListener {

    private final RentalProcessingFacade rentalProcessingFacade;

    public RentalEventListener(RentalProcessingFacade rentalProcessingFacade) {
        this.rentalProcessingFacade = rentalProcessingFacade;
    }

    @EventListener
    public void onRentalCreated(RentalCreatedEvent event) {
        Rental rental = event.getRental();
        if (RentalStatus.pending.equals(rental.getStatus())) {
            rentalProcessingFacade.approveRentABook(rental.getBook().getId(), rental.getUser().getId());
        }
    }

    @EventListener
    public void onRentalReturned(RentalReturnEvent event) {
        Rental rental = event.getRental();
        if (RentalStatus.return_requested.equals(rental.getStatus())) {
            //rentalService.approveReturn(rental.getRentalId());
        }
    }
}
