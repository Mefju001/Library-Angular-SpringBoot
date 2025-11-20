package com.app.library.EventListener;

import com.app.library.Entity.Rental;
import com.app.library.Entity.RentalStatus;
import com.app.library.Facade.RentalProcessingFacade;
import com.app.library.Facade.ReturnProcessingFacade;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RentalEventListener {

    private final RentalProcessingFacade rentalProcessingFacade;
    private final ReturnProcessingFacade  returnProcessingFacade;

    public RentalEventListener(RentalProcessingFacade rentalProcessingFacade, ReturnProcessingFacade   returnProcessingFacade) {
        this.rentalProcessingFacade = rentalProcessingFacade;
        this.returnProcessingFacade = returnProcessingFacade;
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
            returnProcessingFacade.approveReturnBook(rental.getBook().getId(),rental.getUser().getId());
        }
    }
}
