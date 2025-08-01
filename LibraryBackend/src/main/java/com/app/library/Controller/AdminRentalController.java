package com.app.library.Controller;

import com.app.library.DTO.Request.RentalRequest;
import com.app.library.Service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/api/admin/rental")
@Tag(name = "Admin rental Controller", description = "Udostępnia funkcję dla administratora w adminPanelu")
public class AdminRentalController {
    private final RentalService rentalService;

    public AdminRentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PutMapping("/confirm")
    @Operation(
            summary = "Zatwierdza wypożyczenie książki",
            description = "Zatwierdza istniejące żądanie wypożyczenia książki przez użytkownika."
    )
    public ResponseEntity<String> approveLoan(@RequestBody @Valid RentalRequest request) {
        rentalService.approveLoanBook(request.bookId(), request.userId());
        return ResponseEntity.ok("Book loaned successfully.");
    }

    @PutMapping("/return/confirm/{bookId}")
    @Operation(
            summary = "Potwierdzenie zwrotu książki przez administratora",
            description = "Administrator potwierdza zwrot książki o podanym ID.")
    public ResponseEntity<String> approveReturn(
            @Parameter(description = "ID książki do potwierdzenia zwrotu")
            @PathVariable Integer bookId) {
        rentalService.approveReturn(bookId);
        return ResponseEntity.ok("Book returned successfully.");
    }

    //Do edycji
    @PutMapping("/extend/confirm/{bookId}")
    @Operation(summary = "Potwierdzenie przez administratora przedłużenia wypożyczenia książki przez użytkownika")
    public ResponseEntity<String> approveExtendLoan(
            @Parameter(description = "ID książki")
            @PathVariable Integer bookId) {
        try {
            rentalService.approveExtendLoan(bookId);
            return ResponseEntity.ok("Book returned successfully.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/check-overdue")
    @Operation(
            summary = "Sprawdza zaległe wypożyczenia",
            description = "Zwraca listę wypożyczeń, których termin zwrotu już minął."
    )
    public ResponseEntity<Map<String, Object>> checkOverdueRentals() {
        return ResponseEntity.ok(rentalService.checkOverdueRentals());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/requests/check-request")
    @Operation(
            summary = "Zatwierdza wszystkie oczekujące prośby o wypożyczenie",
            description = "Administrator masowo zatwierdza wszystkie nierozpatrzone prośby o wypożyczenie książek."
    )
    public ResponseEntity<String> checkRequestForRentals() {
        rentalService.approveAll();
        return ResponseEntity.ok("All pending loan requests approved.");
    }
}
