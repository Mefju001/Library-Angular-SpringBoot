package com.app.library.Controller;

import com.app.library.DTO.Request.LibraryBookRequest;
import com.app.library.DTO.Request.LibraryRequest;
import com.app.library.DTO.Response.LibraryBookResponse;
import com.app.library.DTO.Response.LibraryResponse;
import com.app.library.Entity.LibraryBook;
import com.app.library.Service.LibraryService;
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
        List<LibraryResponse> libraryResponses = libraryService.findlibrarybylocation(name);
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


}
