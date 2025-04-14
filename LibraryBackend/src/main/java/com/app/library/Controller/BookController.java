package com.app.library.Controller;

import com.app.library.DTO.Request.BookRequest;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.DTO.Response.GenreResponse;
import com.app.library.Service.BookService;
import com.app.library.Service.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Book Controller", description = "Zarządzanie książkami w aplikacji")
public class BookController {
    private final BookService bookService;
    @Autowired
    public BookController(BookService bookService, PromotionService promotionService) {
        this.bookService = bookService;
    }
    @GetMapping("/")
    @Operation(summary = "Zwraca ksiazki z bazy danych", description = "Zwraca dane książek z bazy danych")
    public ResponseEntity<Page<BookResponse>>listofbooks(
            @Parameter(description = "Numer strony paginacji")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Liczba elementów na stronie")
            @RequestParam(defaultValue = "10") int size)
    {

        Page<BookResponse>books=bookService.findall(page,size);
        if(books.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Zwraca książkę po ID", description = "Zwraca szczegóły książki na podstawie identyfikatora.")
    public ResponseEntity<BookResponse>getBookById(@Parameter(description = "Numer identyfikacyjny ksiazki")
                                                       @PathVariable Integer id)
    {
        BookResponse bookResponse = bookService.findbyid(id);
        if (bookResponse == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookResponse);
    }
    @GetMapping("/genres")
    @Operation(summary = "Zwraca gatunki ksiazek z bazy danych", description = "Zwraca gatunki ksiazek z bazy danych")
    public ResponseEntity<List<GenreResponse>>listofgenres()
    {
        List<GenreResponse> genreResponse = bookService.findallgenres();
        if(genreResponse.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(genreResponse);
    }
    @GetMapping("/search/title")
    @Operation(summary = "Zwraca ksiazki o podanej nazwie.", description = "Zwraca szczegóły książek które posiadają wyszukiwany tytuł")
    public ResponseEntity<Page<BookResponse>> listofbooksbytitle
            (@Parameter(description = "Tytuł książki, którego szukasz")
             @RequestParam String title,
             @Parameter(description = "Numer strony paginacji")
             @RequestParam(defaultValue = "0") int page,
             @Parameter(description = "Liczba elementów na stronie")
             @RequestParam(defaultValue = "10") int size)
    {
        Page<BookResponse> bookResponse = bookService.findbooksbytitle(title,page,size);
        if (bookResponse.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookResponse);
    }
    @GetMapping("/search/genre")
    @Operation(summary = "Zwraca książki z bazy danych na podstawie gatunku", description = "Zwraca książki z bazy danych, które pasują do podanego gatunku.")
    public ResponseEntity<Page<BookResponse>> listofbooksbygenre(@Parameter(description = "Nazwa gatunku książki")
                                                                 @RequestParam String genre_name,

                                                                 @Parameter(description = "Numer strony paginacji")
                                                                 @RequestParam(defaultValue = "${pagination.defaultPage:0}") int page,

                                                                 @Parameter(description = "Liczba elementów na stronie")
                                                                 @RequestParam(defaultValue = "${pagination.defaultSize:10}") int size)
    {
        Page<BookResponse> bookResponse = bookService.findbooksbygenre(genre_name,page,size);
        if (bookResponse.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookResponse);
    }
    @GetMapping("/search/publisher")
    @Operation(summary = "Zwraca książki z bazy danych na podstawie wydawnictwa", description = "Zwraca książki z bazy danych, które pasują do podanego wydawnictwa.")
    public ResponseEntity<Page<BookResponse>> listofbooksbypublisher(@Parameter(description = "Nazwa wydawnictwa książki")
                                                                     @RequestParam String publisher_name,

                                                                     @Parameter(description = "Numer strony paginacji")
                                                                     @RequestParam(defaultValue = "${pagination.defaultPage:0}") int page,

                                                                     @Parameter(description = "Liczba elementów na stronie")
                                                                     @RequestParam(defaultValue = "${pagination.defaultSize:10}") int size)
    {
        Page<BookResponse> bookResponse= bookService.findbooksbypublisher(publisher_name,page,size);
        if (bookResponse.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookResponse);
    }
    @GetMapping("/search/author")
    @Operation(summary = "Zwraca książki z bazy danych na podstawie imienia i nazwiska autora ksiazki", description = "Zwraca dane książek z bazy danych, które pasują do podanego imienia i nazwiska autora")
    public ResponseEntity<Page<BookResponse>> listofbooksbyauthor(@Parameter(description = "Imie autora")
                                                                    @RequestParam String name,
                                                                  @Parameter(description = "Nazwisko autora")
                                                                    @RequestParam String surname,
                                                                  @Parameter(description = "Numer strony paginacji")
                                                                    @RequestParam(defaultValue = "${pagination.defaultPage:0}") int page,
                                                                  @Parameter(description = "Liczba elementów na stronie")
                                                                    @RequestParam(defaultValue = "${pagination.defaultSize:10}") int size)
    {
        Page<BookResponse> bookResponse = bookService.findbooksbyauthor(name, surname,page,size);
        if (bookResponse.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookResponse);
    }
    @GetMapping("/search/price")
    @Operation(summary = "Zwraca książki z bazy danych na podstawie dwóch cen minimalnej i maksymalnej", description = "Zwraca dane książek z bazy danych, które wpisują się pomidzy dwie ceny minimalna i maksymalną")
    public ResponseEntity<Page<BookResponse>> listofbooksbyprice(@Parameter(description = "Cena minimalna")
                                                                     @RequestParam Float price1,
                                                                 @Parameter(description = "Cena maksymalna")
                                                                     @RequestParam Float price2,
                                                                 @Parameter(description = "Numer strony paginacji")
                                                                     @RequestParam(defaultValue = "${pagination.defaultPage:0}") int page,
                                                                 @Parameter(description = "Liczba elementów na stronie")
                                                                     @RequestParam(defaultValue = "${pagination.defaultSize:10}") int size) {
        Page<BookResponse> bookResponse = bookService.findbooksbyprice(price1, price2,page,size);
        if (bookResponse.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookResponse);
    }
    @GetMapping("/search/year")
    @Operation(summary = "Zwraca książki z bazy danych w podanym okresie", description = "Zwraca dane książek z bazy danych, które wpasowują sie w dany okres")
    public ResponseEntity<Page<BookResponse>> listofbooksbyyear(@Parameter(description = "Rok początkowy okresu")
                                                                    @RequestParam LocalDate year1,
                                                                @Parameter(description = "Rok końcowy okresu")
                                                                    @RequestParam LocalDate year2,
                                                                @Parameter(description = "Numer strony paginacji")
                                                                    @RequestParam(defaultValue = "${pagination.defaultPage:0}") int page,
                                                                @Parameter(description = "Liczba elementów na stronie")
                                                                    @RequestParam(defaultValue = "${pagination.defaultSize:10}") int size) {
        Page<BookResponse> bookResponse = bookService.findbooksbyyear(year1, year2,page,size);
        if (bookResponse.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookResponse);
    }
    @GetMapping("/sort/title")
    @Operation(summary = "Sortuje ksiazki po tytule.", description = "Zwraca książki z bazy danych posortowane według ASC badz DESC.")
    public ResponseEntity<Page<BookResponse>> listofsortbooksoftitle(@Parameter(description = "Wybór sortowania ASC czy DESC")
                                                                         @RequestParam String name,

                                                                     @Parameter(description = "Numer strony paginacji")
                                                                         @RequestParam(defaultValue = "${pagination.defaultPage:0}") int page,

                                                                     @Parameter(description = "Liczba elementów na stronie")
                                                                         @RequestParam(defaultValue = "${pagination.defaultSize:10}") int size)
    {
        Page<BookResponse> bookResponse= bookService.sortbooktitle(page,size,name);
        if(bookResponse.isEmpty())
        {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookResponse);
    }
    @GetMapping("/sort/price")
    @Operation(summary = "Sortuje ksiazki po cenie.", description = "Zwraca książki z bazy danych posortowane według ASC badz DESC.")
    public ResponseEntity<Page<BookResponse>> listofsortbooksofprice(@Parameter(description = "Wybór sortowania ASC czy DESC")
                                                                         @RequestParam String name,

                                                                     @Parameter(description = "Numer strony paginacji")
                                                                         @RequestParam(defaultValue = "${pagination.defaultPage:0}") int page,

                                                                     @Parameter(description = "Liczba elementów na stronie")
                                                                         @RequestParam(defaultValue = "${pagination.defaultSize:10}") int size)
    {
        Page<BookResponse> bookResponse= bookService.sortbookprice(page,size,name);
        if(bookResponse.isEmpty())
        {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookResponse);
    }
    @GetMapping("/sort/year")
    @Operation(summary = "Sortuje ksiazki po roku.", description = "Zwraca książki z bazy danych posortowane według ASC badz DESC.")
    public ResponseEntity<Page<BookResponse>> listofsortbooksofyear(@Parameter(description = "Wybór sortowania ASC czy DESC")
                                                                        @RequestParam String name,

                                                                    @Parameter(description = "Numer strony paginacji")
                                                                        @RequestParam(defaultValue = "${pagination.defaultPage:0}") int page,

                                                                    @Parameter(description = "Liczba elementów na stronie")
                                                                        @RequestParam(defaultValue = "${pagination.defaultSize:10}") int size)
    {
        Page<BookResponse> bookResponse= bookService.sortbookyear(page,size,name);
        if(bookResponse.isEmpty())
        {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookResponse);
    }
    @GetMapping("/news")
    @Operation(summary = "Zwraca najnowsze książki", description = "Zwraca dane książek, które wyszły tego roku.")
    public ResponseEntity<Page<BookResponse>> listofnewbooks (@Parameter(description = "Numer strony paginacji")
                                                                  @RequestParam(defaultValue = "${pagination.defaultPage:0}") int page,

                                                              @Parameter(description = "Liczba elementów na stronie")
                                                                  @RequestParam(defaultValue = "${pagination.defaultSize:10}") int size)
    {
        Page<BookResponse> bookResponse= bookService.findnewbooks(page,size);
        if(bookResponse.isEmpty())
        {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookResponse);
    }
    @GetMapping("/foreshadowed")
    @Operation(summary = "Zwraca zapowiedzi książkowe", description = "Zwraca dane książek, które dopiero co wyjdą")
    public ResponseEntity<Page<BookResponse>> listofforeshadowedbooks (@Parameter(description = "Numer strony paginacji")
                                                                           @RequestParam(defaultValue = "${pagination.defaultPage:0}") int page,

                                                                       @Parameter(description = "Liczba elementów na stronie")
                                                                           @RequestParam(defaultValue = "${pagination.defaultSize:10}") int size)
    {
        Page<BookResponse> bookResponse= bookService.findforeshadowedbooks(page,size);
        if (bookResponse.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookResponse);
    }
    //////
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    @Operation(summary = "Dodaje ksiazke do bazy danych", description = "Dodaje ksiazke uzupelnioną przez użytkownika do bazy danych")
    public ResponseEntity<BookRequest> addbook(@Parameter(description = "Obiekt zawierający dane książki, które mają zostać dodane do bazy danych")
                                                   @RequestBody @Valid BookRequest bookRequest) {
        BookRequest addedbook = bookService.addbook(bookRequest);
        return ResponseEntity.ok(addedbook);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    @Operation(summary = "Aktualizuje ksiazke do bazy danych", description = "Aktualizuje ksiazke uzupelnioną przez użytkownika do bazy danych")
    public ResponseEntity<BookRequest> updatebook(@Parameter(description = "Numer identyfikacyjny ksiazki")
                                                    @PathVariable Integer id,
                                                  @Parameter(description = "Obiekt zawierający dane książki, które mają zostać zaaktualizowane do bazy danych")
                                                    @RequestBody BookRequest bookRequest) {
        BookRequest updatedBook = bookService.updateBook(id,bookRequest);
        return ResponseEntity.ok(updatedBook);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Usuwa ksiazke z bazy danych", description = "Usuwa ksiazke o podanym ID z bazy danych")
    public ResponseEntity<?> delete(@Parameter(description = "Numer identyfikacyjny ksiazki")
                                        @PathVariable Integer id) {
        try {
            bookService.deletebook(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
