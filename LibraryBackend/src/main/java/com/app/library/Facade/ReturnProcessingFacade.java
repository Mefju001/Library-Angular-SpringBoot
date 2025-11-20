package com.app.library.Facade;

import com.app.library.EventListener.RentalCreatedEvent;
import com.app.library.Facade.Services.PenaltyCalculationService;
import com.app.library.Facade.Services.Rental.ReturnRentalPersistenceService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ReturnProcessingFacade {
    private final ReturnRentalPersistenceService returnRentalPersistenceService;
    private final ApplicationEventPublisher publisher;
    private final PenaltyCalculationService penaltyCalculationService;
    public ReturnProcessingFacade(ReturnRentalPersistenceService returnRentalPersistenceService, ApplicationEventPublisher publisher, PenaltyCalculationService penaltyCalculationService) {
        this.returnRentalPersistenceService = returnRentalPersistenceService;
        this.publisher = publisher;
        this.penaltyCalculationService = penaltyCalculationService;
    }
    public void requestForReturnBook(Integer BookId, Long UserId)
    {
        var request = returnRentalPersistenceService.requestForReturnABook(BookId,UserId);
        publisher.publishEvent(new RentalCreatedEvent(request));
    }
    public void approveReturnBook(Integer BookId, Long UserId)
    {
        var penalty = penaltyCalculationService.calculateExtensionPenalty(BookId,UserId);
        returnRentalPersistenceService.approveReturnABook(penalty,BookId,UserId);
    }
}
