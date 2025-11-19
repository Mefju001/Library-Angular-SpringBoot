package com.app.library.Service;

import com.app.library.DTO.Mapper.RentalBookMapper;
import com.app.library.DTO.Response.RentalBookResponse;
import com.app.library.Entity.*;
import com.app.library.EventListener.RentalCreatedEvent;
import com.app.library.Facade.Rental.RequestRent.RentalProcessingFacade;
import com.app.library.Facade.Rental.RequestReturn.Service.ReturnRentalPersistenceService;
import com.app.library.Repository.BookRepository;
import com.app.library.Repository.RentalRepository;
import com.app.library.Repository.UserRepository;
import com.app.library.Service.Interfaces.RentalService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@EnableScheduling
public class RentalServiceImpl implements RentalService {
    private static final Logger logger = LoggerFactory.getLogger(RentalServiceImpl.class);
    private final RentalRepository rentalRepository;
    private final ReturnRentalPersistenceService returnRentalPersistenceService;
    private final RentalBookMapper rentalBookMapper;
    private final RentalProcessingFacade rentalProcessingFacade;

    @Autowired
    RentalServiceImpl(ApplicationEventPublisher publisher, RentalRepository rentalRepository, BookRepository bookRepository, UserRepository userRepository, ReturnRentalPersistenceService returnRentalPersistenceService, RentalBookMapper rentalBookMapper, RentalProcessingFacade rentalProcessingFacade) {
        this.returnRentalPersistenceService = returnRentalPersistenceService;
        this.publisher = publisher;
        this.rentalRepository = rentalRepository;
        this.rentalBookMapper = rentalBookMapper;
        this.rentalProcessingFacade = rentalProcessingFacade;
    }

    @Override
    public List<RentalBookResponse> rentalList(Long UserId) {
        return rentalRepository.findRentalsByUser_Id(UserId).stream().map(rentalBookMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void requestRentABook(Integer BookId, Long UserId) {
        rentalProcessingFacade.requestForRentABook(BookId, UserId);
    }

    @Override
    @Transactional
    public void requestReturn(Integer BookId, Long UserId) {
        returnRentalPersistenceService.requestForReturnABook(BookId, UserId);
    }

    @Override
    @Transactional
    public void approveReturn(Integer rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wypożyczenia o podanym ID"));

        if (rental.getStatus() != RentalStatus.return_requested) {
            throw new IllegalStateException("To wypożyczenie nie ma zgłoszonego zwrotu");
        }
        rental.setStatus(RentalStatus.returned);
        rental.setRentalEndDate(LocalDate.now());
        Penalty penalty = new Penalty();
        long overdueDays = ChronoUnit.DAYS.between(rental.getRentalEndDate(), rental.getReturnRequestDate());
        Double penaltyPrice = penalty.calculatePenaltyPrice(Math.toIntExact(overdueDays));
        rental.setPenalty(penaltyPrice);
        rentalRepository.save(rental);
    }

    @Override
    @Transactional
    public void approveAll() {
        List<Rental> rentalList = rentalRepository.findRentalsByStatusIn(List.of(RentalStatus.return_requested, RentalStatus.pending, RentalStatus.extend_requested));
        for (Rental rentals : rentalList) {
            if (rentals.getStatus().equals(RentalStatus.return_requested)) {
                rentals.setStatus(RentalStatus.returned);
            } else if (rentals.getStatus().equals(RentalStatus.pending)) {
                rentals.setStatus(RentalStatus.loaned);
            } else if (rentals.getStatus().equals(RentalStatus.extend_requested)) {
                rentals.setStatus(RentalStatus.loaned);
                rentals.setRentalEndDate(rentals.getRentalEndDate().plusMonths(1));
            }
        }
        rentalRepository.saveAll(rentalList);
    }

    @Override
    public LoanDeadlineInfo howManyDaysLeft(Integer bookId, Long userId) {
        Rental rental = rentalRepository.findRentalByBook_IdAndUser_Id(bookId,userId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wypożyczenia o podanym ID"));

        if (rental.getStatus() != RentalStatus.loaned&&rental.getStatus() != RentalStatus.overdue&&rental.getStatus() != RentalStatus.extend_requested) {
            throw new IllegalStateException("Ksiazka musi być wypożyczona");
        }
        LocalDate today = LocalDate.now();
        LocalDate endDate = rental.getRentalEndDate();
        long daysBetween = ChronoUnit.DAYS.between(today, endDate);
        boolean isOverdue = daysBetween < 0;
        return new LoanDeadlineInfo(Math.abs(daysBetween), isOverdue);
    }
    @Override
    public Boolean isOverdue(Integer rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Nie znaleziono wypożyczenia o podanym ID"));

        if (rental.getStatus() != RentalStatus.loaned) {
            throw new IllegalStateException("Ksiazka musi być wypożyczona");
        }
        if(rental.getRentalEndDate().isBefore(LocalDate.now())) {
            rental.setStatus(RentalStatus.overdue);
            rentalRepository.save(rental);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Map<String, Object> checkOverdueRentals() {
        List<Rental> rentalList = rentalRepository.findByStatusInAndRentalEndDateBefore(List.of(RentalStatus.loaned), LocalDate.now());
        int updatedcount = 0;
        for (Rental listRental : rentalList) {
            listRental.setStatus(RentalStatus.overdue);
            updatedcount++;
        }
        rentalRepository.saveAll(rentalList);
        Map<String, Object> response = new HashMap<>();
        response.put("updated", updatedcount);
        return response;
    }

    @Override
    @Transactional
    public void requestExtendLoan(Integer bookId, Long userId) {
        Rental rental = rentalRepository.findRentalByBook_IdAndUser_Id(bookId,userId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wypożyczenia o podanym ID"));

        if (rental.getStatus() != RentalStatus.loaned) {
            throw new IllegalStateException("Ksiazka nie została wypożyczona");
        }

        rental.setStatus(RentalStatus.extend_requested);
        rentalRepository.save(rental);
    }

    @Override
    @Transactional
    public void approveExtendLoan(Integer rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wypożyczenia o podanym ID"));

        if (rental.getStatus() != RentalStatus.extend_requested) {
            throw new IllegalStateException("Ksiazka nie została wypożyczona");
        }
        rental.setStatus(RentalStatus.loaned);
        rental.setRentalEndDate(rental.getRentalEndDate().plusMonths(3));
        rentalRepository.save(rental);
    }

    @Override
    @Transactional
    public void cancelLoanBook(Integer bookId, Long userId) {
        Rental rental = rentalRepository.findRentalByBook_IdAndUser_Id(bookId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wypożyczenia o podanych ID"));

        if (rental.getStatus() != RentalStatus.pending) {
            throw new IllegalStateException("Ksiazka musi być nie wypożyczona");
        }
        rental.setStatus(RentalStatus.cancelled);
        rentalRepository.save(rental);
    }

    @Override
    public Long getActiveBorrowsCount() {
        return rentalRepository.countByStatusIn(List.of(RentalStatus.loaned,RentalStatus.extend_requested));
    }

    @Override
    public Long getOverdueCount() {
        return rentalRepository.countByStatusIn(List.of(RentalStatus.overdue));
    }
}
