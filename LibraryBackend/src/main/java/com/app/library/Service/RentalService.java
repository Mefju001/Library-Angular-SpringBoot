package com.app.library.Service;

import com.app.library.Entity.*;
import com.app.library.Repository.BookRepository;
import com.app.library.Repository.RentalRepository;
import com.app.library.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {
    private static final Logger logger = LoggerFactory.getLogger(RentalService.class);
    RentalRepository rentalRepository;
    BookRepository bookRepository;
    UserRepository userRepository;

    @Autowired
    RentalService(RentalRepository rentalRepository, BookRepository bookRepository, UserRepository userRepository)
    {
        this.rentalRepository = rentalRepository;
        this.bookRepository =bookRepository;
        this.userRepository = userRepository;
    }
    public List<Rental>rentalList(Long UserId)
    {
        return rentalRepository.findRentalsByUser_Id(UserId);
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
        int overdueDays = rental.getDays(); // Zakładamy, że getDays() zwraca liczbę dni opóźnienia
        Double penaltyPrice = penalty.calculatePenaltyPrice(overdueDays); // Obliczamy karę
        rental.setPenalty(penaltyPrice);
        rentalRepository.save(rental);
    }
}
