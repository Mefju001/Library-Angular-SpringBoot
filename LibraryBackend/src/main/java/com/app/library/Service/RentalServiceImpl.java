package com.app.library.Service;

import com.app.library.DTO.Mapper.LoanBookMapper;
import com.app.library.DTO.Response.LoanBookResponse;
import com.app.library.Entity.*;
import com.app.library.EventListener.RentalCreatedEvent;
import com.app.library.Repository.BookRepository;
import com.app.library.Repository.RentalRepository;
import com.app.library.Repository.UserRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class RentalServiceImpl implements RentalService {
    private static final Logger logger = LoggerFactory.getLogger(RentalServiceImpl.class);
    private final RentalRepository rentalRepository;
    private final ApplicationEventPublisher publisher;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanBookMapper loanBookMapper;

    @Autowired
    RentalServiceImpl(ApplicationEventPublisher publisher, RentalRepository rentalRepository, BookRepository bookRepository, UserRepository userRepository, LoanBookMapper loanBookMapper) {
        this.publisher = publisher;
        this.rentalRepository = rentalRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.loanBookMapper = loanBookMapper;
    }

    @Override
    public List<LoanBookResponse> rentalList(Long UserId) {
        List<Rental> loanbooks = rentalRepository.findRentalsByUser_Id(UserId);
        List<LoanBookResponse> rentalList = loanbooks.stream().map(loanBookMapper::toloanBookResponse).collect(Collectors.toList());
        return rentalList;
    }

    @Override
    @Transactional
    public void requestloanBook(Integer BookId, Long UserId) {
        List<Rental> rentalList = rentalRepository.findRentalsByUser_IdAndStatusIs(UserId, RentalStatus.loaned);
        if (rentalList.size() >= 5) {
            throw new IllegalStateException("Cannot loan more than 5 books.");
        }
        Book book = bookRepository.findById(BookId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        User user = userRepository.findById(UserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Rental rental = new Rental();
        rental.setBook(book);
        rental.setUser(user);
        rental.requestStartLoan();
        rentalRepository.save(rental);
        publisher.publishEvent(new RentalCreatedEvent(rental));
    }

    @Override
    @Transactional
    public void approveLoanBook(Integer BookId, Long UserId) {
        Optional<Rental> loanbook = rentalRepository.findRentalByBook_IdAndUser_Id(BookId, UserId);
        if (loanbook.isPresent() && loanbook.get().getStatus() == RentalStatus.pending) {
            Rental rental = loanbook.get();
            rental.startLoan();
            rentalRepository.save(rental);
        } else {
            throw new IllegalStateException("This book is not loaned by you");
        }
    }

    @Override
    @Transactional
    public void requestReturn(Integer BookId, Long UserId) {
        Optional<Rental> loanbook = rentalRepository.findRentalByBook_IdAndUser_Id(BookId, UserId);
        if (loanbook.isPresent() && loanbook.get().getStatus() == RentalStatus.loaned) {
            Rental rental = loanbook.get();
            rental.requestEndLoan();
            rentalRepository.save(rental);
            publisher.publishEvent(new RentalCreatedEvent(rental));
        } else {
            throw new IllegalStateException("This book is not loaned by you");

        }
    }

    @Override
    @Transactional
    public void approveReturn(Integer rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wypożyczenia o podanym ID"));

        if (rental.getStatus() != RentalStatus.return_requested) {
            throw new IllegalStateException("To wypożyczenie nie ma zgłoszonego zwrotu");
        }
        rental.endLoan();
        Penalty penalty = new Penalty();
        int overdueDays = rental.getDays();
        Double penaltyPrice = penalty.calculatePenaltyPrice(overdueDays);
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
                rentals.setStatus(RentalStatus.extend);
                rentals.setRentalEndDate(rentals.getRentalEndDate().plusMonths(1));
            }
        }
        rentalRepository.saveAll(rentalList);
    }

    @Override
    public LoanDeadlineInfo howManyDaysLeft(Integer rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wypożyczenia o podanym ID"));

        if (rental.getStatus() != RentalStatus.loaned) {
            throw new IllegalStateException("Ksiazka musi być wypożyczona");
        }
        return rental.getRemainingDays();
    }

    @Override
    public Boolean isOverdue(Integer rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wypożyczenia o podanym ID"));

        if (rental.getStatus() != RentalStatus.loaned) {
            throw new IllegalStateException("Ksiazka musi być wypożyczona");
        }
        return rental.isOverdue();
    }

    @Override
    @Transactional
    public Map<String, Object> checkOverdueRentals() {
        List<Rental> rentalList = rentalRepository.findByStatusInAndRentalEndDateBefore(List.of(RentalStatus.loaned, RentalStatus.extend), LocalDate.now());
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
    public void requestExtendLoan(Integer rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
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
        rental.extendLoan();
        rentalRepository.save(rental);
    }

    @Override
    @Transactional
    public void cancelLoanBook(Integer rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wypożyczenia o podanym ID"));

        if (rental.getStatus() != RentalStatus.pending) {
            throw new IllegalStateException("Ksiazka musi być nie wypożyczona");
        }
        rental.cancelLoan();
        rentalRepository.save(rental);
    }

    @Override
    public Long getActiveBorrowsCount() {
        return rentalRepository.countByStatusIn(List.of(RentalStatus.loaned, RentalStatus.extend));
    }

    @Override
    public Long getOverdueCount() {
        return rentalRepository.countByStatusIn(List.of(RentalStatus.overdue));
    }
}
