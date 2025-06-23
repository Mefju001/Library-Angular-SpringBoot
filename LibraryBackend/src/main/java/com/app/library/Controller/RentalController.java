package com.app.library.Controller;

import com.app.library.DTO.Request.LoanRequest;
import com.app.library.DTO.Response.LoanBookResponse;
import com.app.library.Entity.LoanDeadlineInfo;
import com.app.library.Service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rental")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Pobiera wypożyczenia użytkownika")
    public ResponseEntity<List<LoanBookResponse>> getUserRentals(
            @Parameter(description = "ID użytkownika") @PathVariable Long userId) {
        try {
            List<LoanBookResponse> rentalList = rentalService.rentalList(userId);
            return ResponseEntity.ok(rentalList);
        } catch (ResponseStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping("/loan")
    @Operation(summary = "Wypożycza książkę dla użytkownika")
    public ResponseEntity<String> loanBook(@RequestBody @Valid LoanRequest request) {
        rentalService.requestloanBook(request.bookId(), request.userId());
        return ResponseEntity.ok("Book request loaned successfully.");
    }

    @PutMapping("/loan/return-request")
    @Operation(
            summary = "Zgłasza chęć zwrotu książki",
            description = "Użytkownik zgłasza chęć zwrotu wypożyczonej książki. przekazując identyfikatory książki i użytkownika w treści żądania."
    )
    public ResponseEntity<String> requestReturn(@RequestBody @Valid LoanRequest request) {
            rentalService.requestReturn(request.bookId(), request.userId());
            return ResponseEntity.ok("Book return request submitted successfully.");
    }
    @GetMapping("/loan/{bookId}/days-left")
    @Operation(summary = "Sprawdza ile dni pozostało do końca wypożyczenia książki")
    public ResponseEntity<LoanDeadlineInfo> howManyDaysLeft(
            @Parameter(description = "ID książki")
            @PathVariable Integer bookId) {
        LoanDeadlineInfo data = rentalService.howManyDaysLeft(bookId);
        return ResponseEntity.ok(data);
    }
    //do edycji
    @PutMapping("/loan/extend/request/{userId}/{bookId}")
    @Operation(summary = "Zgłasza prośbę o przedłużenie wypożyczenia książki dla użytkownika")
    public ResponseEntity<String> requestExtendLoan(
            @Parameter(description = "ID użytkownika")
            @PathVariable Long userId,
            @Parameter(description = "ID książki")
            @PathVariable Integer bookId) {
        try {
            rentalService.requestExtendLoan(bookId);
            return ResponseEntity.ok("Loan extension request submitted successfully.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
    @PutMapping("/cancel/requestforloan/{bookId}")
    @Operation(
            summary = "Anuluje prośbę o wypożyczenie książki",
            description = "Administrator anuluje oczekującą prośbę o wypożyczenie książki przez użytkownika na podstawie ID książki."
    )
    public ResponseEntity<String> cancelLoanBook(
            @Parameter(description = "ID książki, dla której anulowana zostaje prośba o wypożyczenie")
            @PathVariable Integer bookId) {
        try {
            rentalService.cancelLoanBook(bookId);
            return ResponseEntity.ok("Loan request canceled successfully.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}