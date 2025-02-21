package com.app.library.Controller;

import com.app.library.DTO.Request.BookRequest;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.DTO.Response.GenreResponse;
import com.app.library.Entity.Book;
import com.app.library.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {
    private final BookService bookService;
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    @GetMapping("/")
    public ResponseEntity<List<BookResponse>>listofbooks()
    {
        return bookService.findall();
    }
    @GetMapping("/genres")
    public ResponseEntity<List<GenreResponse>>listofgenres()
    {
        return bookService.findallgenres();
    }
    @GetMapping("/search/title")
    public ResponseEntity<List<BookResponse>> listofbooksbytitle(@RequestParam String title)
    {
        return bookService.findbooksbytitle(title);
    }
    @GetMapping("/search/genre")
    public ResponseEntity<List<BookResponse>> listofbooksbygenre(@RequestParam String genre_name)
    {
        return bookService.findbooksbygenre(genre_name);
    }
    @GetMapping("/search/publisher")
    public ResponseEntity<List<BookResponse>> listofbooksbypublisher(@RequestParam String publisher_name)
    {
        return bookService.findbooksbypublisher(publisher_name);
    }
    @GetMapping("/search/author")
    public ResponseEntity<List<BookResponse>> listofbooksbyauthor(@RequestParam String name, @RequestParam String surname) {
        return bookService.findbooksbyauthor(name, surname);
    }
    @GetMapping("/search/price")
    public ResponseEntity<List<BookResponse>> listofbooksbyprice(@RequestParam float min, @RequestParam float max) {
        return bookService.findbooksbyprice(min, max);
    }
    @GetMapping("/search/year")
    public ResponseEntity<List<BookResponse>> listofbooksbyyear(@RequestParam Integer year1, @RequestParam Integer year2) {
        return bookService.findbooksbyyear(year1, year2);
    }
    //////
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<BookRequest> addbook(@RequestBody BookRequest bookRequest) {
        return bookService.addbook(bookRequest);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updatebook(@RequestBody BookRequest bookRequest) {
        return bookService.updateBook(bookRequest);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Integer id) {
        return bookService.deletebook(id);
    }
}
