package com.app.library.Controller;

import com.app.library.DTO.Request.BookRequest;
import com.app.library.DTO.Request.LibraryBookRequest;
import com.app.library.DTO.Request.LibraryRequest;
import com.app.library.DTO.Request.LoanRequest;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.DTO.Response.DashboardStatsResponse;
import com.app.library.DTO.Response.LibraryBookResponse;
import com.app.library.DTO.Response.LibraryResponse;
import com.app.library.Entity.LibraryBook;
import com.app.library.Service.BookService;
import com.app.library.Service.LibraryService;
import com.app.library.Service.RentalService;
import com.app.library.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/adminPanel")
@Tag(name = "Admin Controller", description = "Udostępnia funkcję dla administratora w adminPanelu")
public class AdminController {
    private final BookService bookService;
    private final UserService userService;
    private final LibraryService libraryService;
    private final RentalService rentalService;
    @Autowired
    public AdminController(BookService bookService, UserService userService, LibraryService libraryService, RentalService rentalService) {
        this.bookService = bookService;
        this.userService = userService;
        this.libraryService = libraryService;
        this.rentalService = rentalService;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dashboard/stats")
    @Operation(summary = "Pobiera statystyki dashboardu", description = "Zwraca ilość użytkowników, wypożyczeń, nowych książek i zaległości")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats()
    {
        Long userCount = userService.getUserCount();
        Long loanCount = rentalService.getActiveBorrowsCount();
        Long newBooksCount = bookService.getNewBooksCount();
        Long overdueCount = rentalService.getOverdueCount();
        return ResponseEntity.ok(new DashboardStatsResponse(userCount,loanCount,newBooksCount,overdueCount));
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    @Operation(
            summary = "Dodaje nową książkę",
            description = """
        Tworzy nowy rekord książki w bazie danych.
        Endpoint dostępny wyłącznie dla administratorów (ROLE_ADMIN).
        """)

    public ResponseEntity<BookRequest> addBook(@Parameter(description = "Obiekt zawierający dane książki, które mają zostać dodane do bazy danych")
                                                       @RequestBody @Valid BookRequest bookRequest) {
        BookRequest addedbook = bookService.addbook(bookRequest);
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
            @RequestBody @Valid BookRequest bookRequest){
        BookRequest updatedBook = bookService.updateBook(id,bookRequest);
        return ResponseEntity.ok(updatedBook);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Usuwa książkę z bazy danych",
            description = "Usuwa książkę z bazy danych na podstawie jej identyfikatora. Wymagane są uprawnienia administratora. Zwraca kod 204, jeśli operacja zakończy się sukcesem, lub 404, jeśli książka nie zostanie znaleziona."
    )    public ResponseEntity<?> deleteBook(
            @Parameter(description = "Identyfikator książki do usunięcia", example = "123", required = true)
            @PathVariable Integer id) {
        try {
            bookService.deletebook(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/libraries")
    @Operation(
            summary = "Dodaje bibliotekę",
            description = "Tworzy nowy obiekt biblioteki na podstawie danych przesłanych przez użytkownika i zapisuje go w bazie danych."
    )
    public ResponseEntity<LibraryResponse>addLibrary(
            @Parameter(description = "Dane biblioteki do utworzenia")
            @RequestBody @Valid LibraryRequest library)
    {
        return ResponseEntity.ok(libraryService.addlibrary(library));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/libraries/{id}")
    @Operation(
            summary = "Aktualizuje bibliotekę",
            description = "Aktualizuje dane biblioteki na podstawie przesłanego identyfikatora i nowych danych."
    )
    public ResponseEntity<LibraryResponse>updateLibrary(
            @Parameter(description = "ID biblioteki do zaktualizowania")
            @PathVariable Integer id,

            @Parameter(description = "Nowe dane biblioteki")
            @RequestBody @Valid LibraryRequest library)
    {
        LibraryResponse updatedLibrary = libraryService.updatelibrary(id, library);
        if (updatedLibrary == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedLibrary);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/libraries/{id}")
    @Operation(
            summary = "Usuwa bibliotekę",
            description = "Usuwa bibliotekę na podstawie podanego ID. Jeśli biblioteka nie istnieje, zwraca kod 404."
    )
    public ResponseEntity<?>deleteLibrary(
            @Parameter(description = "ID biblioteki do usunięcia")
            @PathVariable Integer id
    )
    {
        try {
            libraryService.deletelibrary(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/library-books")
    @Operation(
            summary = "Dodaje książkę do biblioteki",
            description = "Tworzy powiązanie między książką a biblioteką, umożliwiając śledzenie jej dostępności."
    )
    public ResponseEntity<LibraryBookResponse>addBookToLibrary(
            @Parameter(description = "Dane książki i biblioteki do powiązania")
            @RequestBody @Valid LibraryBookRequest request){
        return ResponseEntity.ok(libraryService.addbooktolibrary(request));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/library-books")
    @Operation(
            summary = "Aktualizuje powiązanie książki z biblioteką",
            description = "Aktualizuje dane o dostępności książki w danej bibliotece."
    )
    public ResponseEntity<LibraryBookResponse>updateBookInLibrary(
            @Parameter(description = "Dane do aktualizacji książki w bibliotece")
            @RequestBody @Valid LibraryBookRequest request)
    {
        LibraryBookResponse updatedLibraryBook = libraryService.updatebookandlibrary(request);
        if (updatedLibraryBook == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedLibraryBook);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/library-books/{id}")
    @Operation(
            summary = "Usuwa książkę z biblioteki",
            description = "Usuwa powiązanie książki z biblioteką na podstawie identyfikatora. Zwraca 404, jeśli nie istnieje."
    )
    public ResponseEntity<LibraryBook>deleteBookFromLibrary(
            @Parameter(description = "ID powiązania książki z biblioteką")
            @PathVariable Integer id)
    {
        try {
            libraryService.deletebookandlibrary(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/loan/confirm")
    @Operation(
            summary = "Zatwierdza wypożyczenie książki",
            description = "Zatwierdza istniejące żądanie wypożyczenia książki przez użytkownika."
    )
    public ResponseEntity<String> approveLoan(@RequestBody @Valid LoanRequest request) {
        rentalService.approveLoanBook(request.bookId(),request.userId());
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
    @PutMapping("/loan/extend/confirm/{bookId}")
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
    public ResponseEntity<Map<String, Object>> checkOverdueRentals()
    {
        return ResponseEntity.ok(rentalService.checkOverdueRentals());
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/loans/requests/check-request")
    @Operation(
            summary = "Zatwierdza wszystkie oczekujące prośby o wypożyczenie",
            description = "Administrator masowo zatwierdza wszystkie nierozpatrzone prośby o wypożyczenie książek."
    )
    public ResponseEntity<String> checkRequestForRentals()
    {
        rentalService.approveAll();
        return ResponseEntity.ok("All pending loan requests approved.");
    }
}
