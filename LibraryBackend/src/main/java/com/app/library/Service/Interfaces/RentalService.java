package com.app.library.Service.Interfaces;

import com.app.library.DTO.Response.RentalBookResponse;
import com.app.library.Entity.LoanDeadlineInfo;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;

public interface RentalService {
    List<RentalBookResponse> rentalList(Long userId);
    void requestRentABook(Integer bookId, Long userId);
    void requestReturn(Integer bookId, Long userId);
    LoanDeadlineInfo howManyDaysLeft(Integer bookId, Long userId);
    void requestExtendLoan(Integer bookId, Long userId);
    void approveExtendLoan(Integer bookId);
    void cancelLoanBook(Integer bookId, Long userId);
    Long getActiveBorrowsCount();
    Long getOverdueCount();

}
