package com.app.library.Facade.Rental.RequestReturn;

import com.app.library.EventListener.RentalCreatedEvent;
import com.app.library.Facade.Rental.RequestReturn.Service.ReturnRentalPersistenceService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ReturnProcessingFacade {
    private final ReturnRentalPersistenceService returnRentalPersistenceService;
    private final ApplicationEventPublisher publisher;
    public ReturnProcessingFacade(ReturnRentalPersistenceService returnRentalPersistenceService, ApplicationEventPublisher publisher) {
        this.returnRentalPersistenceService = returnRentalPersistenceService;
        this.publisher = publisher;
    }
    public void requestForReturnBook(Integer BookId, Long UserId)
    {
        var request = returnRentalPersistenceService.requestForReturnABook(BookId,UserId);
        publisher.publishEvent(new RentalCreatedEvent(request));
    }
    /*Penalty penalty = new Penalty();
        long overdueDays = ChronoUnit.DAYS.between(rental.getRentalEndDate(), rental.getReturnRequestDate());
        Double penaltyPrice = penalty.calculatePenaltyPrice(Math.toIntExact(overdueDays));*/
}
