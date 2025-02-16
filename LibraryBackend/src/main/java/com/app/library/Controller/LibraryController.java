package com.app.library.Controller;

import com.app.library.DTO.Response.LibraryResponse;
import com.app.library.Entity.Library;
import com.app.library.Service.LibraryService;
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
    public ResponseEntity<List<Library>> listoflibraries()
    {
        return libraryService.findall();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/")
    public ResponseEntity<Library>addlibrary(@RequestBody Library library)
    {
        return libraryService.addlibrary(library);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/")
    public ResponseEntity<Library>updatelibrary(@RequestBody Library library)
    {
        return libraryService.updatelibrary(library);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/")
    public ResponseEntity<Library>deletelibrary(@RequestBody Integer library)
    {
        return libraryService.deletelibrary(library);
    }
}
