package com.app.library.Facade.Rental.RequestReturn.Service;

import com.app.library.Entity.Rental;
import com.app.library.Entity.RentalStatus;
import com.app.library.Repository.RentalRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReturnRentalPersistenceService {
    private final RentalRepository rentalRepository;

    public ReturnRentalPersistenceService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Transactional
    public Rental requestForReturnABook(Integer BookId, Long UserId)
    {
        var request = rentalRepository.findRentalByBook_IdAndUser_Id(BookId,UserId).orElseThrow(EntityNotFoundException::new);
        if(request.getStatus() == RentalStatus.loaned)
        {
            request.setReturnRequestDate(LocalDate.now());
            request.setStatus(RentalStatus.return_requested);
            request = rentalRepository.save(request);
            return request;
        }
        else
        {
            throw new IllegalStateException("This book is not loaned by you");
        }
    }
}
