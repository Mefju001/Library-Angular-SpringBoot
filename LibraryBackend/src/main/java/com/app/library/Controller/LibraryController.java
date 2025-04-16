package com.app.library.Controller;

import com.app.library.DTO.Request.LibraryBookRequest;
import com.app.library.DTO.Request.LibraryRequest;
import com.app.library.DTO.Response.LibraryBookResponse;
import com.app.library.DTO.Response.LibraryResponse;
import com.app.library.Entity.LibraryBook;
import com.app.library.Service.LibraryService;
import com.app.library.Service.LibraryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Library Controller", description = "Zarządzanie bibliotekami w aplikacji")
public class LibraryController {
    private final LibraryService libraryService;
    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/")
    @Operation(summary = "Zwraca biblioteki", description = "Zwraca informacje o bibliotekach z bazy danych")
    public ResponseEntity<List<LibraryResponse>> listoflibraries()
    {
        List<LibraryResponse> libraryResponses = libraryService.findall();
        if(libraryResponses.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(libraryResponses);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Zwraca biblioteke o podanym ID", description = "Zwraca informacje o bibliotece o podanym id z bazy danych")
    public ResponseEntity<LibraryResponse> listoflibrarybyId(@Parameter(description = "Numer identyfikacyjny ksiazki")
                                                                 @PathVariable Integer id)
    {
        LibraryResponse libraryResponse = libraryService.findbyid(id);
        if(libraryResponse==null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(libraryResponse);
    }
    @GetMapping("/search/name{name}")
    @Operation(summary = "Zwraca biblioteke o podanej nazwie", description = "Zwraca informacje o bibliotece o podanej nazwie z bazy danych")
    public ResponseEntity<List<LibraryResponse>> listoflibrariesbyname(@Parameter(description = "Nazwa biblioteki")
                                                                           @PathVariable String name)
    {
        List<LibraryResponse> libraryResponses = libraryService.findlibrarybyname(name);
        if(libraryResponses.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(libraryResponses);
    }
    @GetMapping("/searchby/{title}")
    @Operation(summary = "Zwraca dostepnosc ksiazki o podanej nazwie", description = "Zwraca informacje o bibliotekach gdzie ksiazka jest dostepna")
    public ResponseEntity<List<LibraryBookResponse>> listofbookinlibraries(@Parameter(description = "Nazwa ksiazki")
                                                                       @PathVariable String title)
    {
        List<LibraryBookResponse> libraryBookResponses = libraryService.findbookinlibraries(title);
        if(libraryBookResponses.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(libraryBookResponses);
    }
    @GetMapping("/booksinlibrary")
    @Operation(summary = "Zwraca ksiazki dostepne w bibliotekach", description = "Zwraca ksiazki dostepne w bibliotekach z bazy danych")
    public ResponseEntity<List<LibraryBookResponse>> listofbooksinlibraries()
    {
        List<LibraryBookResponse> libraryBookResponses = libraryService.findallbookandlibrary();
        if(libraryBookResponses.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(libraryBookResponses);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addlibrary")
    @Operation(summary = "Dodaje biblioteke", description = "Dodaje do bazy danych obiekt z uzupełnionymi danymi przez uzytkownika")
    public ResponseEntity<LibraryResponse>addlibrary(@Parameter(description = "Obiekt zawierający dane biblioteki, które mają zostać dodane do bazy danych")
                                                 @RequestBody @Valid LibraryRequest library)
    {
        return ResponseEntity.ok(libraryService.addlibrary(library));
    }
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addbooktolibrary")
    @Operation(summary = "Dodaje dostepnosc ksiazki w bibliotece", description = "Dodaje do bazy danych obiekt zawierający informacje o książce i bibliotece, " +
            "co pozwala na śledzenie dostępności książek w systemie bibliotecznym.")
    public ResponseEntity<LibraryBookResponse>addbooktolibrary(@Parameter(description = "Obiekt zawierający dane bilioteki i ksiazki, które mają zostać dodane do bazy danych")
                                                           @RequestBody @Valid LibraryBookRequest libraryBookRequest)
    {
        return ResponseEntity.ok(libraryService.addbooktolibrary(libraryBookRequest));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    @Operation(
            summary = "Aktualizuje dane biblioteki",
            description = "Aktualizuje dane biblioteki w systemie na podstawie przekazanego obiektu 'Library'."
    )
    public ResponseEntity<LibraryResponse>updatelibrary(@Parameter(description = "Numer identyfikacyjny biblioteki, która ma zostać zaktualizowana")
                                                    @PathVariable Integer id,

                                                @Parameter(description = "Obiekt zawierający dane biblioteki do aktualizacji")
                                                    @RequestBody @Valid LibraryRequest library)
    {
        LibraryResponse updatedLibrary = libraryService.updatelibrary(id, library);
        if (updatedLibrary == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedLibrary);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updatebooksinlibrary")
    @Operation(
            summary = "Aktualizuje książki w bibliotece",
            description = "Aktualizuje dane książki w bibliotece na podstawie przekazanego obiektu 'LibraryBook'."
    )
    public ResponseEntity<LibraryBookResponse>updatebooksinlibrary(@Parameter(description = "Obiekt zawierający dane książki w bibliotece do zaktualizowania")
                                                               @RequestBody @Valid LibraryBookRequest libraryBook)
    {
        LibraryBookResponse updatedLibraryBook = libraryService.updatebookandlibrary(libraryBook);

        // Sprawdzamy, czy książka została zaktualizowana
        if (updatedLibraryBook == null) {
            // Jeśli książka nie została znaleziona, zwracamy odpowiedź 404
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedLibraryBook);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Usuwa bibliotekę na podstawie ID", description = "Usuwa bibliotekę o podanym identyfikatorze. Jeśli nie ma takiej biblioteki, zwraca 404.")
    public ResponseEntity<?>deletelibrary(@Parameter(description = "Numer identyfikacyjny biblioteki, która ma zostać usunieta")
                                              @PathVariable Integer id)
    {
        try {
            libraryService.deletelibrary(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deletebookandlibrary")
    @Operation(summary = "Usuwa książkę z biblioteki", description = "Usuwa książkę i jej powiązania z biblioteką na podstawie ID. Jeśli książka lub biblioteka nie istnieje, zwraca 404.")
    public ResponseEntity<LibraryBook>deletebookandlibrary(@Parameter(description = "Numer identyfikacyjny obiektu LibraryBook")
                                                               @RequestParam Integer id)
    {
        try {
            libraryService.deletebookandlibrary(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
