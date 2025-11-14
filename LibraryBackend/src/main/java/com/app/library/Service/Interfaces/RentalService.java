package com.app.library.Service.Interfaces;

import com.app.library.DTO.Response.RentalBookResponse;
import com.app.library.Entity.LoanDeadlineInfo;

import java.util.List;
import java.util.Map;

public interface RentalService {
    List<RentalBookResponse> rentalList(Long userId);

    void requestloanBook(Integer bookId, Long userId);

    void approveLoanBook(Integer bookId, Long userId);

    void requestReturn(Integer bookId, Long userId);

    void approveReturn(Integer rentalId);

    void approveAll();

    LoanDeadlineInfo howManyDaysLeft(Integer bookId, Long userId);

    Boolean isOverdue(Integer rentalId);

    Map<String, Object > checkOverdueRentals();

    void requestExtendLoan(Integer bookId, Long userId);

    void approveExtendLoan(Integer rentalId);

    void cancelLoanBook(Integer bookId, Long userId);

    Long getActiveBorrowsCount();

    Long getOverdueCount();

}
