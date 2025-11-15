package com.app.library.Controller;

import com.app.library.DTO.Request.BookRequest;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.DTO.Response.DashboardStatsResponse;
import com.app.library.Service.Interfaces.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/api/adminPanel")
@Tag(name = "Admin Controller", description = "Udostępnia funkcję dla administratora w adminPanelu")
public class AdminController {
    private final BookService bookService;
    private final UserService userService;
    private final RentalService rentalService;

    @Autowired
    public AdminController(BookService bookService, UserService userService, PromotionService promotionService, LibraryService libraryService, RentalService rentalService) {
        this.bookService = bookService;
        this.userService = userService;
        this.rentalService = rentalService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dashboard/stats")
    @Operation(summary = "Pobiera statystyki dashboardu", description = "Zwraca ilość użytkowników, wypożyczeń, nowych książek i zaległości")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        Long userCount = userService.getUserCount();
        Long loanCount = rentalService.getActiveBorrowsCount();
        Long newBooksCount = bookService.getNewBooksCount();
        Long overdueCount = rentalService.getOverdueCount();
        return ResponseEntity.ok(new DashboardStatsResponse(userCount, loanCount, newBooksCount, overdueCount));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    @Operation(
            summary = "Dodaje nową książkę",
            description = """
                    Tworzy nowy rekord książki w bazie danych.
                    Endpoint dostępny wyłącznie dla administratorów (ROLE_ADMIN).
                    """)

    public ResponseEntity<BookResponse> addBook(@Parameter(description = "Obiekt zawierający dane książki, które mają zostać dodane do bazy danych")
                                               @RequestBody @Valid BookRequest bookRequest) {
        var addedbook = bookService.addbook(bookRequest);
        return ResponseEntity.ok(addedbook);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    @Operation(
            summary = "Aktualizuje książkę w bazie danych",
            description = "Aktualizuje dane istniejącej książki w bazie danych na podstawie przesłanego identyfikatora i nowych danych. Wymagane są uprawnienia administratora."
    )
    public ResponseEntity<BookRequest> updateBook(
            @Parameter(description = "Identyfikator książki, która ma zostać zaktualizowana", example = "123")
            @PathVariable Integer id,

            @Parameter(description = "Zaktualizowane dane książki", required = true)
            @RequestBody @Valid BookRequest bookRequest) {
        BookRequest updatedBook = bookService.updateBook(id, bookRequest);
        return ResponseEntity.ok(updatedBook);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Usuwa książkę z bazy danych",
            description = "Usuwa książkę z bazy danych na podstawie jej identyfikatora. Wymagane są uprawnienia administratora. Zwraca kod 204, jeśli operacja zakończy się sukcesem, lub 404, jeśli książka nie zostanie znaleziona."
    )
    public ResponseEntity<?> deleteBook(
            @Parameter(description = "Identyfikator książki do usunięcia", example = "123", required = true)
            @PathVariable Integer id) {
        try {
            bookService.deletebook(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
