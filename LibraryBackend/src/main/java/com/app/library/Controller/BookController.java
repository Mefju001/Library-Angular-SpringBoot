package com.app.library.Controller;

import com.app.library.DTO.Request.BookSearchCriteria;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.DTO.Response.GenreResponse;
import com.app.library.Entity.BookImg;
import com.app.library.Service.Interfaces.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Book Controller", description = "Zarządzanie książkami w aplikacji")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    @Operation(summary = "Zwraca ksiazki z bazy danych", description = "Zwraca dane książek z bazy danych")
    public ResponseEntity<Page<BookResponse>> Pageofbooks(
            @Parameter(description = "Numer strony paginacji")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Liczba elementów na stronie")
            @RequestParam(defaultValue = "10") int size) {

        Page<BookResponse> books = bookService.findall(page, size);
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books")
    @Operation(summary = "Zwraca ksiazki z bazy danych", description = "Zwraca dane książek z bazy danych")
    public ResponseEntity<List<BookResponse>> listofbooks() {

        List<BookResponse> books = bookService.findAllList();
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }



    @GetMapping("/bookImg/{id}")
    @Operation(summary = "Zwraca książkę po ID", description = "Zwraca szczegóły książki na podstawie identyfikatora.")
    public ResponseEntity<BookImg> getBookImgById(@Parameter(description = "Numer identyfikacyjny ksiazki")
                                                  @PathVariable Integer id) {
        BookImg bookImg = bookService.findByBookId(id);
        return ResponseEntity.ok(bookImg);
    }

    @GetMapping("/genres")
    @Operation(summary = "Zwraca gatunki ksiazek z bazy danych", description = "Zwraca gatunki ksiazek z bazy danych")
    public ResponseEntity<List<GenreResponse>> listofgenres() {
        List<GenreResponse> genreResponse = bookService.findallgenres();
        if (genreResponse.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(genreResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Zwraca książkę po ID", description = "Zwraca szczegóły książki na podstawie identyfikatora.")
    public ResponseEntity<BookResponse> getBookById(@Parameter(description = "Numer identyfikacyjny ksiazki")
                                                    @PathVariable Integer id) {
        BookResponse bookResponse = bookService.findbyid(id);
        if (bookResponse == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookResponse);
    }

    @GetMapping("/Search")
    @Operation(summary = "",description = "")
    public ResponseEntity<Page<BookResponse>>searchBooks(BookSearchCriteria criteria){
        Page<BookResponse> bookResponse = bookService.searchBooks(criteria);
        return ResponseEntity.ok(bookResponse);
    }
    @GetMapping("/Sort")
    @Operation(summary = "Sortuje ksiazki po tytule.", description = "Zwraca książki z bazy danych posortowane według ASC badz DESC.")
    public ResponseEntity<Page<BookResponse>> sortBooks
    (@Parameter(description = "Pole do sortowania (np. 'title', 'price', 'publicationYear')")
    @RequestParam String sortBy,

    @Parameter(description = "Kierunek sortowania ('ASC' lub 'DESC')")
    @RequestParam(defaultValue = "ASC") String direction,

    @Parameter(description = "Numer strony paginacji")
    @RequestParam(defaultValue = "${pagination.defaultPage:0}") int page,

    @Parameter(description = "Liczba elementów na stronie")
    @RequestParam(defaultValue = "${pagination.defaultSize:10}") int size) {
        Page<BookResponse> bookResponse = bookService.sortBooks(page, size, sortBy,direction);
        if (bookResponse.isEmpty()) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookResponse);
    }
}
