package com.app.library.Facade.Services;

import com.app.library.Entity.Penalty;
import com.app.library.Repository.RentalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

@Service
public class PenaltyCalculationService {
    private final RentalRepository rentalRepository;
    @Autowired
    public PenaltyCalculationService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public double calculateExtensionPenalty(Integer BookId,Long UserId) {
        var request = rentalRepository.findRentalByBook_IdAndUser_Id(BookId,UserId).orElseThrow(EntityNotFoundException::new);
        Penalty penalty = new Penalty();
        long overdue = ChronoUnit.DAYS.between(request.getRentalEndDate(), request.getReturnRequestDate());
        return penalty.calculatePenaltyPrice(Math.toIntExact(overdue));
    }
}
