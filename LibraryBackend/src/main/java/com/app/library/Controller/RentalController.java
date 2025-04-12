package com.app.library.Controller;

import com.app.library.Entity.Rental;
import com.app.library.Service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }
    @GetMapping("/loan/{userId}")
    @Operation(summary = "Wypożycza ksiazke dla użytkownika")
    public ResponseEntity<List<Rental>> showloanbooks(@Parameter(description = "ID użytkownika")
                                                        @PathVariable Long userId) {
        try {
            List<Rental> rentalList = rentalService.rentalList(userId);
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
    @Operation(summary = "Wypożycza ksiazke dla użytkownika")
    public ResponseEntity<String> approveReturn(@Parameter(description = "ID użytkownika")
                                                @PathVariable Integer bookId) {
        try {
            rentalService.approveReturn(bookId);
            return ResponseEntity.ok("Book returned successfully.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}
