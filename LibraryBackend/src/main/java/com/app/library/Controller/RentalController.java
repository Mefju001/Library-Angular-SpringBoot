package com.app.library.Controller;

import com.app.library.DTO.Request.RentalRequest;
import com.app.library.DTO.Response.RentalBookResponse;
import com.app.library.Entity.LoanDeadlineInfo;
import com.app.library.Service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/rental")
@Tag(name = "Rental Controller", description = "Obsługuje operacje związane z wypożyczeniami książek")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Pobiera wypożyczenia użytkownika")
    public ResponseEntity<List<RentalBookResponse>> getUserRentals(
            @Parameter(description = "ID użytkownika") @PathVariable Long userId) {
        try {
            List<RentalBookResponse> rentalList = rentalService.rentalList(userId);
            return ResponseEntity.ok(rentalList);
        } catch (ResponseStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/")
    @Operation(summary = "Wypożycza książkę dla użytkownika")
    public ResponseEntity<String> loanBook(@RequestBody @Valid RentalRequest request) {
        rentalService.requestloanBook(request.bookId(), request.userId());
        return ResponseEntity.ok("Book request loaned successfully.");
    }

    @PutMapping("/return-request")
    @Operation(
            summary = "Zgłasza chęć zwrotu książki",
            description = "Użytkownik zgłasza chęć zwrotu wypożyczonej książki. przekazując identyfikatory książki i użytkownika w treści żądania."
    )
    public ResponseEntity<String> requestReturn(@RequestBody @Valid RentalRequest request) {
        rentalService.requestReturn(request.bookId(), request.userId());
        return ResponseEntity.ok("Book return request submitted successfully.");
    }

    @GetMapping("/book/{bookId}/user/{userId}/days-left")
    @Operation(summary = "Sprawdza ile dni pozostało do końca wypożyczenia książki")
    public ResponseEntity<LoanDeadlineInfo> howManyDaysLeft(
            @Parameter(description = "ID użytkownika")
            @PathVariable Long userId,
            @Parameter(description = "ID książki")
            @PathVariable Integer bookId) {
        LoanDeadlineInfo data = rentalService.howManyDaysLeft(bookId,userId);
        return ResponseEntity.ok(data);
    }

    //do edycji
    @PutMapping("/extend/request/{userId}/{bookId}")
    @Operation(summary = "Zgłasza prośbę o przedłużenie wypożyczenia książki dla użytkownika")
    public ResponseEntity<String> requestExtendLoan(
            @Parameter(description = "ID użytkownika")
            @PathVariable Long userId,
            @Parameter(description = "ID książki")
            @PathVariable Integer bookId) {
        try {
            rentalService.requestExtendLoan(bookId,userId);
            return ResponseEntity.ok("Loan extension request submitted successfully.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @PutMapping("/cancel/{bookId}/{userId}")
    @Operation(
            summary = "Anuluje prośbę o wypożyczenie książki",
            description = "Administrator anuluje oczekującą prośbę o wypożyczenie książki przez użytkownika na podstawie ID książki."
    )
    public ResponseEntity<String> cancelLoanBook(
            @Parameter(description = "ID użytkownika")
            @PathVariable Long userId,
            @Parameter(description = "ID książki")
            @PathVariable Integer bookId) {
        try {
            rentalService.cancelLoanBook(bookId,userId);
            return ResponseEntity.ok("Loan request canceled successfully.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}