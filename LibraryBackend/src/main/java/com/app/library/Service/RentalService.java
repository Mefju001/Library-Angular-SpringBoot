package com.app.library.Service;

import com.app.library.Entity.Book;
import com.app.library.Entity.Rental;
import com.app.library.Entity.User;
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
    public void loanBook(Integer BookId,Long UserId)
    {
        List<Rental> rentalList = rentalRepository.findRentalsByUser_Id(UserId);
        if(rentalList.size()>=5)
        {
            throw new IllegalStateException("Cannot loan more than 5 books.");
        }
        Book book= bookRepository.findById(BookId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        User user= userRepository.findById(UserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Rental rental = new Rental();
        rental.setBook(book);
        rental.setUser(user);
        rental.startLoan();
        rentalRepository.save(rental);
    }
}
