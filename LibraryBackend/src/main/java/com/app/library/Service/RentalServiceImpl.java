package com.app.library.Service;

import com.app.library.DTO.Mapper.RentalBookMapper;
import com.app.library.DTO.Response.RentalBookResponse;
import com.app.library.Entity.*;
import com.app.library.Facade.RentalProcessingFacade;
import com.app.library.Facade.ReturnProcessingFacade;
import com.app.library.Repository.RentalRepository;
import com.app.library.Service.Interfaces.RentalService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@EnableScheduling
public class RentalServiceImpl implements RentalService {
    private static final Logger logger = LoggerFactory.getLogger(RentalServiceImpl.class);
    private final RentalRepository rentalRepository;
    private final ReturnProcessingFacade returnProcessingFacade;
    private final RentalBookMapper rentalBookMapper;
    private final RentalProcessingFacade rentalProcessingFacade;

    @Autowired
    RentalServiceImpl(RentalRepository rentalRepository, ReturnProcessingFacade returnProcessingFacade, RentalBookMapper rentalBookMapper, RentalProcessingFacade rentalProcessingFacade) {
        this.returnProcessingFacade = returnProcessingFacade;
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
        returnProcessingFacade.requestForReturnBook(BookId, UserId);
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
    @Transactional
    public void requestExtendLoan(Integer bookId, Long userId) {
        rentalProcessingFacade.requestExtendRent(bookId, userId);
    }

    @Transactional
    @Override
    public void approveExtendLoan(Integer bookId) {
        var name = SecurityContextHolder.getContext().getAuthentication().getName();
        rentalProcessingFacade.approveExtendRent(bookId, name);
    }

    @Override
    @Transactional
    public void cancelLoanBook(Integer bookId, Long userId) {
        rentalProcessingFacade.cancelRentBook(bookId, userId);
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
