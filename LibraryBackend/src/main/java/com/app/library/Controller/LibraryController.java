package com.app.library.Controller;

import com.app.library.DTO.Response.LibraryBookResponse;
import com.app.library.DTO.Response.LibraryResponse;
import com.app.library.Service.Interfaces.LibraryInventoryService;
import com.app.library.Service.Interfaces.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@PreAuthorize("hasRole('ROLE_USER')")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Library Controller", description = "Zarządzanie bibliotekami w aplikacji")
public class LibraryController {
    private final LibraryService libraryService;
    private final LibraryInventoryService libraryInventoryService;

    @Autowired
    public LibraryController(LibraryService libraryService, LibraryInventoryService libraryInventoryService) {
        this.libraryService = libraryService;
        this.libraryInventoryService = libraryInventoryService;
    }

    @GetMapping("/")
    @Operation(summary = "Zwraca biblioteki", description = "Zwraca informacje o bibliotekach z bazy danych")
    public ResponseEntity<List<LibraryResponse>> listoflibraries() {
        List<LibraryResponse> libraryResponses = libraryService.findall();
        if (libraryResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(libraryResponses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Zwraca biblioteke o podanym ID", description = "Zwraca informacje o bibliotece o podanym id z bazy danych")
    public ResponseEntity<LibraryResponse> listoflibrarybyId(@Parameter(description = "Numer identyfikacyjny ksiazki")
                                                             @PathVariable Integer id) {
        LibraryResponse libraryResponse = libraryService.findbyid(id);
        if (libraryResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(libraryResponse);
    }

    @GetMapping("/searchby/{title}")
    @Operation(summary = "Zwraca dostepnosc ksiazki o podanej nazwie", description = "Zwraca informacje o bibliotekach gdzie ksiazka jest dostepna")
    public ResponseEntity<List<LibraryBookResponse>> listofbookinlibraries(@Parameter(description = "Nazwa ksiazki")
                                                                           @PathVariable String title) {
        List<LibraryBookResponse> libraryBookResponses = libraryInventoryService.findbookByTitleInLibraries(title);
        if (libraryBookResponses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(libraryBookResponses);
    }

    @GetMapping("/booksinlibrary")
    @Operation(summary = "Zwraca ksiazki dostepne w bibliotekach", description = "Zwraca ksiazki dostepne w bibliotekach z bazy danych")
    public ResponseEntity<List<LibraryBookResponse>> listofbooksinlibraries() {
        List<LibraryBookResponse> libraryBookResponses = libraryInventoryService.findallbookandlibrary();
        if (libraryBookResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(libraryBookResponses);
    }


}
