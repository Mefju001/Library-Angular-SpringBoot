package com.app.library.Controller;

import com.app.library.DTO.Request.LibraryBookRequest;
import com.app.library.DTO.Request.LibraryRequest;
import com.app.library.DTO.Response.LibraryBookResponse;
import com.app.library.DTO.Response.LibraryResponse;
import com.app.library.Entity.LibraryBook;
import com.app.library.Service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/admin/library")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminLibraryController {
    private final LibraryService libraryService;

    public AdminLibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
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
    public ResponseEntity<LibraryResponse> updateLibrary(
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
    @PutMapping("/library-books/{id}")
    @Operation(
            summary = "Aktualizuje powiązanie książki z biblioteką",
            description = "Aktualizuje dane o dostępności książki w danej bibliotece."
    )
    public ResponseEntity<LibraryBookResponse>updateBookInLibrary(
            @Parameter(description = "Dane do aktualizacji książki w bibliotece")
            @RequestBody @Valid LibraryBookRequest request,
            @PathVariable int id)
    {
        LibraryBookResponse updatedLibraryBook = libraryService.updatebookandlibrary(id,request);
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
}
