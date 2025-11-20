package com.app.library.Controller;

import com.app.library.DTO.Request.BookCriteria;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.DTO.Response.GenreResponse;
import com.app.library.Entity.BookImg;
import com.app.library.Service.Interfaces.BookService;
import com.app.library.Service.Interfaces.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Book Controller", description = "Zarządzanie książkami w aplikacji")
public class BookController {
    private final BookService bookService;
    private final GenreService genreService;

    @Autowired
    public BookController(BookService bookService, GenreService genreService) {
        this.bookService = bookService;
        this.genreService = genreService;
    }

    @GetMapping("/")
    @Operation(summary = "Zwraca ksiazki z bazy danych", description = "Zwraca dane książek z bazy danych")
    public ResponseEntity<Page<BookResponse>> Pageofbooks(
            @Parameter(description = "Numer strony paginacji")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Liczba elementów na stronie")
            @RequestParam(defaultValue = "10") int size) {

        Page<BookResponse> books = bookService.findAll(page, size);
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
        BookImg bookImg = bookService.findBookImgById(id);
        return ResponseEntity.ok(bookImg);
    }

    @GetMapping("/genres")
    @Operation(summary = "Zwraca gatunki ksiazek z bazy danych", description = "Zwraca gatunki ksiazek z bazy danych")
    public ResponseEntity<List<GenreResponse>> listofgenres() {
        List<GenreResponse> genreResponse = List.of(); //= genreService.();
        if (genreResponse.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(new ArrayList<>());//to do
    }

    @GetMapping("/{id}")
    @Operation(summary = "Zwraca książkę po ID", description = "Zwraca szczegóły książki na podstawie identyfikatora.")
    public ResponseEntity<BookResponse> getBookById(@Parameter(description = "Numer identyfikacyjny ksiazki")
                                                    @PathVariable Integer id) {
        BookResponse bookResponse = bookService.findById(id);
        if (bookResponse == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookResponse);
    }

    @GetMapping("/SearchOrSort")
    @Operation(summary = "",description = "")
    public ResponseEntity<Page<BookResponse>>searchBooks(BookCriteria criteria){
        Page<BookResponse> bookResponse = bookService.searchOrSortBooksByCriteria(criteria);
        return ResponseEntity.ok(bookResponse);
    }

}
