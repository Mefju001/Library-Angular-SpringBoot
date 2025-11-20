package com.app.library.Controller;

import com.app.library.DTO.Request.RentalRequest;
import com.app.library.DTO.Response.RentalBookResponse;
import com.app.library.Entity.LoanDeadlineInfo;
import com.app.library.Security.Service.UserDetailsImpl;
import com.app.library.Service.Interfaces.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/user")
    @Operation(summary = "Pobiera wypożyczenia użytkownika")
    public ResponseEntity<List<RentalBookResponse>> getUserRentals(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<RentalBookResponse> rentalList = rentalService.rentalList(userDetails.getId());
            return ResponseEntity.ok(rentalList);
        } catch (ResponseStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/")
    @Operation(summary = "Wypożycza książkę dla użytkownika")
    public ResponseEntity<String> loanBook(@RequestBody @Valid RentalRequest request,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        rentalService.requestRentABook(request.bookId(), userDetails.getId());
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

    @GetMapping("/book/{bookId}/days-left")
    @Operation(summary = "Sprawdza ile dni pozostało do końca wypożyczenia książki")
    public ResponseEntity<LoanDeadlineInfo> howManyDaysLeft(@AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "ID książki")
            @PathVariable Integer bookId) {
        LoanDeadlineInfo data = rentalService.howManyDaysLeft(bookId,userDetails.getId());
        return ResponseEntity.ok(data);
    }

    //do edycji
    @PutMapping("/extend/request/{bookId}")
    @Operation(summary = "Zgłasza prośbę o przedłużenie wypożyczenia książki dla użytkownika")
    public ResponseEntity<String> requestExtendLoan(@AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "ID książki")
            @PathVariable Integer bookId) {
        try {
            rentalService.requestExtendLoan(bookId,userDetails.getId());
            return ResponseEntity.ok("Loan extension request submitted successfully.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @PutMapping("/cancel/{bookId}")
    @Operation(
            summary = "Anuluje prośbę o wypożyczenie książki",
            description = "Administrator anuluje oczekującą prośbę o wypożyczenie książki przez użytkownika na podstawie ID książki."
    )
    public ResponseEntity<String> cancelLoanBook(@AuthenticationPrincipal  UserDetailsImpl userDetails,
            @Parameter(description = "ID książki")
            @PathVariable Integer bookId) {
        try {
            rentalService.cancelLoanBook(bookId,userDetails.getId());
            return ResponseEntity.ok("Loan request canceled successfully.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}