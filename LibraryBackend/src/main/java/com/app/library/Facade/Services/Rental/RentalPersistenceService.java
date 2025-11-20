package com.app.library.Facade.Services.Rental;

import com.app.library.Builder.Rental.IRentalBuilder;
import com.app.library.Entity.Rental;
import com.app.library.Entity.RentalStatus;
import com.app.library.Facade.Services.PenaltyCalculationService;
import com.app.library.Repository.BookRepository;
import com.app.library.Repository.RentalRepository;
import com.app.library.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RentalPersistenceService {
    private final RentalRepository rentalRepository;
    private final IRentalBuilder builder;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final PenaltyCalculationService penaltyCalculationService;
    @Autowired
    public RentalPersistenceService(RentalRepository rentalRepository, IRentalBuilder builder, BookRepository bookRepository, UserRepository userRepository, PenaltyCalculationService penaltyCalculationService) {
        this.rentalRepository = rentalRepository;
        this.builder = builder;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.penaltyCalculationService = penaltyCalculationService;
    }
    @Transactional
    public Rental createPendingRental(Integer BookId, Long UserId) {
        var book = bookRepository.findById(BookId).orElseThrow(EntityNotFoundException::new);
        var user = userRepository.findById(UserId).orElseThrow(EntityNotFoundException::new);
        var rental = builder.CreateNewRental(book,user, RentalStatus.pending)
                .build();
        rental = rentalRepository.save(rental);
        return rental;
    }
    @Transactional
    public void approveRentalBook(Integer BookId, Long UserId) {
        var updatedRental = rentalRepository.findRentalByBook_IdAndUser_Id(BookId,UserId).orElseThrow(EntityNotFoundException::new);
        updatedRental.approveLoan();
        rentalRepository.save(updatedRental);
    }
    @Transactional
    public void requestExtendLoan(Integer BookId, Long UserId) {
        var rental = rentalRepository.findRentalByBook_IdAndUser_Id(BookId,UserId).orElseThrow(EntityNotFoundException::new);
        if(rental.getStatus() != RentalStatus.loaned && rental.getStatus() != RentalStatus.overdue){
            throw new IllegalStateException("Rental is not in loaned status");
        }
        if(rental.getStatus().equals(RentalStatus.overdue)){
            rental.setPenalty(penaltyCalculationService.calculateExtensionPenalty(BookId,UserId));
        }
        rental.setStatus(RentalStatus.extend_requested);
        rentalRepository.save(rental);
    }
    @Transactional
    public void approveExtend(Integer BookId, String name) {
        var userId = userRepository.findByUsername(name).orElseThrow(EntityNotFoundException::new);
        var rental = rentalRepository.findRentalByBook_IdAndUser_Id(BookId,userId.getId()).orElseThrow(EntityNotFoundException::new);
        if (rental.getStatus() != RentalStatus.extend_requested) {
            throw new IllegalStateException("Ksiazka nie została wypożyczona");
        }
        rental.setStatus(RentalStatus.loaned);
        rental.setRentalEndDate(rental.getRentalEndDate().plusMonths(3));
        rentalRepository.save(rental);
    }
    @Transactional
    public void cancelLoanABook(Integer bookId, Long userId) {
        var request = rentalRepository.findRentalByBook_IdAndUser_Id(bookId,userId).orElseThrow(EntityNotFoundException::new);

        if (request.getStatus() != RentalStatus.pending) {
            throw new IllegalStateException("Ksiazka musi być nie wypożyczona");
        }
        request.setStatus(RentalStatus.cancelled);
        rentalRepository.save(request);
    }
}
