package com.app.library.Facade.Rental.RequestRent;

import com.app.library.EventListener.RentalCreatedEvent;
import com.app.library.Facade.Rental.RequestRent.Service.RentalPersistenceService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class RentalProcessingFacade {
    private final RentalPersistenceService persistenceService;
    private final ApplicationEventPublisher publisher;
    @Autowired
    public RentalProcessingFacade(RentalPersistenceService rentalPersistenceService, ApplicationEventPublisher publisher) {
        this.persistenceService = rentalPersistenceService;
        this.publisher = publisher;
    }
    @Transactional
    public void requestForRentABook(Integer BookId, Long UserId)
    {
        var rental = persistenceService.createPendingRental(BookId, UserId);
        publisher.publishEvent(new RentalCreatedEvent(rental));
    }
    public void approveRentABook(Integer BookId, Long UserId)
    {
        persistenceService.approveRentalBook(BookId, UserId);
    }
}
