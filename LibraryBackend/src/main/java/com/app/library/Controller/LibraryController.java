package com.app.library.Controller;

import com.app.library.DTO.Response.LibraryBookResponse;
import com.app.library.DTO.Response.LibraryResponse;
import com.app.library.Entity.Library;
import com.app.library.Entity.LibraryBook;
import com.app.library.Service.LibraryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@CrossOrigin(origins = "http://localhost:4200")
public class LibraryController {
    private final LibraryService libraryService;
    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/")
    public ResponseEntity<List<LibraryResponse>> listoflibraries()
    {
        List<LibraryResponse> libraryResponses = libraryService.findall();
        return ResponseEntity.ok(libraryResponses);
    }
    @GetMapping("/{id}")
    public ResponseEntity<LibraryResponse> listoflibrarybyId(@PathVariable Integer id)
    {
        LibraryResponse libraryResponse = libraryService.findbyid(id);
        return ResponseEntity.ok(libraryResponse);
    }
    @GetMapping("/search/name{name}")
    public ResponseEntity<List<LibraryResponse>> listoflibrariesbyname(@PathVariable String name)
    {
        List<LibraryResponse> libraryResponses = libraryService.findlibrarybyname(name);
        return ResponseEntity.ok(libraryResponses);
    }
    @GetMapping("/booksinlibrary")
    public ResponseEntity<List<LibraryBookResponse>> listofbooksinlibraries()
    {
        List<LibraryBookResponse> libraryBookResponses = libraryService.findallbookandlibrary();
        return ResponseEntity.ok(libraryBookResponses);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addlibrary")
    public ResponseEntity<Library>addlibrary(@RequestBody Library library)
    {
        return ResponseEntity.ok(libraryService.addlibrary(library));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addbooktolibrary")
    public ResponseEntity<LibraryBook>addbooktolibrary(@RequestBody LibraryBook libraryBook)
    {
        return ResponseEntity.ok(libraryService.addbooktolibrary(libraryBook));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Library>updatelibrary(@PathVariable Integer id,@RequestBody Library library)
    {
        return ResponseEntity.ok(libraryService.updatelibrary(id,library));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updatebooksinlibrary")
    public ResponseEntity<LibraryBook>updatebooksinlibrary(@RequestBody LibraryBook libraryBook)
    {
        return ResponseEntity.ok(libraryService.updatebookandlibrary(libraryBook));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>deletelibrary(@PathVariable Integer id)
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
    public ResponseEntity<LibraryBook>deletebookandlibrary(@RequestParam Integer id)
    {
        try {
            libraryService.deletebookandlibrary(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
