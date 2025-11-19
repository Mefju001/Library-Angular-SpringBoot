package com.app.library.Facade.Rental.RequestRent.Service;

import com.app.library.Builder.Rental.IRentalBuilder;
import com.app.library.Entity.Rental;
import com.app.library.Entity.RentalStatus;
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
    @Autowired
    public RentalPersistenceService(RentalRepository rentalRepository, IRentalBuilder builder, BookRepository bookRepository, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.builder = builder;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
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
}
