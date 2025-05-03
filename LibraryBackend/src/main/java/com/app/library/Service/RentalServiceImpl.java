package com.app.library.Service;

import com.app.library.DTO.Mapper.BookMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentalServiceImpl implements RentalService{
    private static final Logger logger = LoggerFactory.getLogger(RentalServiceImpl.class);
    private final RentalRepository rentalRepository;
    private final ApplicationEventPublisher publisher;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanBookMapper loanBookMapper;

    @Autowired
    RentalServiceImpl(ApplicationEventPublisher publisher,RentalRepository rentalRepository, BookRepository bookRepository, UserRepository userRepository, LoanBookMapper loanBookMapper)
    {
        this.publisher = publisher;
        this.rentalRepository = rentalRepository;
        this.bookRepository =bookRepository;
        this.userRepository = userRepository;
        this.loanBookMapper = loanBookMapper;
    }
    public List<LoanBookResponse>rentalList(Long UserId)
    {
        List<Rental>loanbooks = rentalRepository.findRentalsByUser_Id(UserId);
        List<LoanBookResponse>rentalList = loanbooks.stream().map(loanBookMapper::toloanBookResponse).collect(Collectors.toList());
        return rentalList;
    }
    @Transactional
    public void requestloanBook(Integer BookId,Long UserId) {
        List<Rental> rentalList = rentalRepository.findRentalsByUser_IdAndStatusIs(UserId,RentalStatus.loaned);
        if(rentalList.size()>=5)
        {
            throw new IllegalStateException("Cannot loan more than 5 books.");
        }
        Book book= bookRepository.findById(BookId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        User user= userRepository.findById(UserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Rental rental = new Rental();
        rental.setBook(book);
        rental.setUser(user);
        rental.requestStartLoan();
        rentalRepository.save(rental);
        publisher.publishEvent(new RentalCreatedEvent(rental));
    }
    @Transactional
    public void approveLoanBook(Integer BookId,Long UserId)
    {
        Optional<Rental> loanbook = rentalRepository.findRentalByBook_IdAndUser_Id(BookId,UserId);
        if(loanbook.isPresent()&&loanbook.get().getStatus()== RentalStatus.pending)
        {
            Rental rental = loanbook.get();
            rental.startLoan();
            rentalRepository.save(rental);
        }else {
            throw new IllegalStateException("This book is not loaned by you");
        }
    }
    @Transactional
    public void requestReturn(Integer BookId,Long UserId)
    {
        Optional<Rental> loanbook = rentalRepository.findRentalByBook_IdAndUser_Id(BookId,UserId);
        if(loanbook.isPresent()&&loanbook.get().getStatus()== RentalStatus.loaned)
        {
            Rental rental = loanbook.get();
            rental.requestEndLoan();
            rentalRepository.save(rental);
            publisher.publishEvent(new RentalCreatedEvent(rental));
        }else {
            throw new IllegalStateException("This book is not loaned by you");

        }
    }
    @Transactional
    public void approveReturn(Integer rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wypożyczenia o podanym ID"));

        if (rental.getStatus() != RentalStatus.return_requested) {
            throw new IllegalStateException("To wypożyczenie nie ma zgłoszonego zwrotu");
        }
        rental.endLoan();
        Penalty penalty =new Penalty();
        int overdueDays = rental.getDays();
        Double penaltyPrice = penalty.calculatePenaltyPrice(overdueDays);
        rental.setPenalty(penaltyPrice);
        rentalRepository.save(rental);
    }

    public LoanDeadlineInfo howManyDaysLeft(Integer rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wypożyczenia o podanym ID"));

        if (rental.getStatus() != RentalStatus.loaned) {
            throw new IllegalStateException("Ksiazka musi być wypożyczona");
        }
        return rental.getRemainingDays();
    }
    public Boolean isOverdue(Integer rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wypożyczenia o podanym ID"));

        if (rental.getStatus() != RentalStatus.loaned) {
            throw new IllegalStateException("Ksiazka musi być wypożyczona");
        }
        return rental.isOverdue();
    }

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
}
