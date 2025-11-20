package com.app.library.Facade;

import com.app.library.EventListener.RentalCreatedEvent;
import com.app.library.Facade.Services.Rental.RentalPersistenceService;
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
    public void requestForRentABook(Integer BookId, Long UserId)
    {
        var rental = persistenceService.createPendingRental(BookId, UserId);
        publisher.publishEvent(new RentalCreatedEvent(rental));
    }
    public void approveRentABook(Integer BookId, Long UserId)
    {
        persistenceService.approveRentalBook(BookId, UserId);
    }
    public void requestExtendRent(Integer BookId, Long UserId)
    {
        persistenceService.requestExtendLoan(BookId, UserId);
    }
    public void approveExtendRent(Integer BookId, String name)
    {
        persistenceService.approveExtend(BookId, name);
    }
    public void cancelRentBook(Integer BookId, Long UserId)
    {
        persistenceService.cancelLoanABook(BookId,UserId);
    }
}
