package com.app.library.Service;
import com.app.library.DTO.Response.LoanBookResponse;
import com.app.library.Entity.LoanDeadlineInfo;
import com.app.library.Entity.Rental;
import com.app.library.Entity.RentalStatus;

import java.util.List;
public interface RentalService {
    List<LoanBookResponse> rentalList(Long userId);

    void requestloanBook(Integer bookId, Long userId);

    void approveLoanBook(Integer bookId, Long userId);

    void requestReturn(Integer bookId, Long userId);

    void approveReturn(Integer rentalId);

    LoanDeadlineInfo howManyDaysLeft(Integer rentalId);

    Boolean isOverdue(Integer rentalId);

    void requestExtendLoan(Integer rentalId);

    void approveExtendLoan(Integer rentalId);

    void cancelLoanBook(Integer rentalId);
    Long getActiveBorrowsCount();
}
