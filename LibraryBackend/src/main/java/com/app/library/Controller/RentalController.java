package com.app.library.Controller;

import com.app.library.DTO.Response.LoanBookResponse;
import com.app.library.Entity.LoanDeadlineInfo;
import com.app.library.Service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }
    @GetMapping("/loan/{userId}")
    @Operation(summary = "Wypożycza ksiazke dla użytkownika")
    public ResponseEntity<List<LoanBookResponse>> showloanbooks(@Parameter(description = "ID użytkownika")
                                                        @PathVariable Long userId) {
        try {
            List<LoanBookResponse> rentalList = rentalService.rentalList(userId);
            return ResponseEntity.ok(rentalList);
        } catch (ResponseStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping("/loan/request/{userId}/{bookId}")
    @Operation(summary = "Wypożycza ksiazke dla użytkownika")
    public ResponseEntity<String> loanBook(@Parameter(description = "ID użytkownika")
                                               @PathVariable Long userId,
                                           @Parameter(description = "ID ksiazki")
                                               @PathVariable Integer bookId) {
        try {
            rentalService.requestloanBook(bookId, userId);
            return ResponseEntity.ok("Book request loaned successfully.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
    @PutMapping("/loan/confirm/{userId}/{bookId}")
    @Operation(summary = "Wypożycza ksiazke dla użytkownika")
    public ResponseEntity<String> approveLoan(@Parameter(description = "ID użytkownika")
                                                  @PathVariable Long userId,
                                              @Parameter(description = "ID ksiazki")
                                                  @PathVariable Integer bookId) {
        try {
            rentalService.approveLoanBook(bookId,userId);
            return ResponseEntity.ok("Book loaned successfully.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
    @PutMapping("/return/request/{userId}/{bookId}")
    @Operation(summary = "Wypożycza ksiazke dla użytkownika")
    public ResponseEntity<String> requestReturn(@Parameter(description = "ID użytkownika")
                                           @PathVariable Long userId,
                                           @Parameter(description = "ID ksiazki")
                                           @PathVariable Integer bookId) {
        try {
            rentalService.requestReturn(bookId, userId);
            return ResponseEntity.ok("Book return request successfully.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
    @PutMapping("/return/confirm/{bookId}")
    @Operation(summary = "potwierdzenie przez administratora zwrotu ksiazki przez użytkownika")
    public ResponseEntity<String> approveReturn(@Parameter(description = "ID użytkownika")
                                                @PathVariable Integer bookId) {
        try {
            rentalService.approveReturn(bookId);
            return ResponseEntity.ok("Book returned successfully.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
    @GetMapping("/loan/howManyDaysLeft/{bookId}")
    @Operation(summary = "")
    public ResponseEntity<LoanDeadlineInfo> howManyDaysLeft(@Parameter(description = "ID użytkownika")
                                                @PathVariable Integer bookId) {
        LoanDeadlineInfo data = rentalService.howManyDaysLeft(bookId);
        return ResponseEntity.ok(data);
    }
    @PutMapping("/loan/extend/request/{userId}/{bookId}")
    @Operation(summary = "Wypożycza ksiazke dla użytkownika")
    public ResponseEntity<String> requestExtendLoan(@Parameter(description = "ID użytkownika")
                                                @PathVariable Long userId,
                                                @Parameter(description = "ID ksiazki")
                                                @PathVariable Integer bookId) {
        try {
            rentalService.requestExtendLoan(bookId);
            return ResponseEntity.ok("Book return request successfully.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
    @PutMapping("/loan/extend/confirm/{bookId}")
    @Operation(summary = "potwierdzenie przez administratora zwrotu ksiazki przez użytkownika")
    public ResponseEntity<String> approveExtendLoan(@Parameter(description = "ID użytkownika")
                                                @PathVariable Integer bookId) {
        try {
            rentalService.approveExtendLoan(bookId);
            return ResponseEntity.ok("Book returned successfully.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
    @PutMapping("/cancel/requestforloan/{bookId}")
    @Operation(summary = "potwierdzenie przez administratora zwrotu ksiazki przez użytkownika")
    public ResponseEntity<String> cancelLoanBook(@Parameter(description = "ID użytkownika")
                                                    @PathVariable Integer bookId) {
        try {
            rentalService.cancelLoanBook(bookId);
            return ResponseEntity.ok("Book returned successfully.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
    @PostMapping("/check-overdue")
    @Operation(summary = "", description = "")
    public ResponseEntity<Map<String, Object>> checkOverdueRentals()
    {
        return ResponseEntity.ok(rentalService.checkOverdueRentals());
    }
    @PostMapping("/check-request")
    @Operation(summary = "", description = "")
    public ResponseEntity<String> checkRequestForRentals()
    {
        rentalService.approveAll();
        return ResponseEntity.ok("Checked and approve");
    }
}