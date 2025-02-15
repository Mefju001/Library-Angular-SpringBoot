package com.app.library.Controller;

import com.app.library.Entity.Book;
import com.app.library.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {
    private final BookService bookService;
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/books")
    public ResponseEntity<List<Book>>listofBooks()
    {
        return bookService.findall();
    }
    @GetMapping("/books/search/title")
    public ResponseEntity<List<Book>> listofbooksbytitle(@RequestParam String title)
    {
        return bookService.findbooksbytitle(title);
    }
    @GetMapping("/books/search/genre")
    public ResponseEntity<List<Book>> listofbooksbygenre(@RequestParam String genre_name)
    {
        return bookService.findbooksbygenre(genre_name);
    }
    @GetMapping("/books/search/publisher")
    public ResponseEntity<List<Book>> listofbooksbypublisher(@RequestParam String publisher_name)
    {
        return bookService.findbooksbypublisher(publisher_name);
    }
    @GetMapping("/books/search/author")
    public ResponseEntity<List<Book>> listofbooksbyauthor(@RequestParam String name, @RequestParam String surname) {
        return bookService.findbooksbyauthor(name, surname);
    }
    @GetMapping("/books/search/price")
    public ResponseEntity<List<Book>> listofbooksbyprice(@RequestParam float min, @RequestParam float max) {
        return bookService.findbooksbyprice(min, max);
    }
    //////
    @PostMapping("/books")
    public ResponseEntity<Book> addbook(@RequestBody Book book) {
        return bookService.addbook(book);
    }

    @PutMapping("/books")
    public ResponseEntity<Book> updatebook(@RequestBody Book book) {
        return bookService.updateBook(book);
    }
    @DeleteMapping("/books")
    public ResponseEntity<Book> delete(@RequestParam Integer id) {
        return bookService.deletebook(id);
    }
}
