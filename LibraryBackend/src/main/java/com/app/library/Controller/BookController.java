package com.app.library.Controller;

import com.app.library.DTO.Request.BookRequest;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.DTO.Response.GenreResponse;
import com.app.library.Service.BookService;
import com.app.library.Service.PromotionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {
    @Autowired
    private PromotionService promotionService;
    private final BookService bookService;
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    @GetMapping("/")
    public ResponseEntity<Page<BookResponse>>listofbooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {

        Page<BookResponse>books=bookService.findall(page,size);
        return ResponseEntity.ok(books);
    }


    @GetMapping("/promotion")
    public void setpromontionspriceonbook(@RequestParam Integer bookId,
                                          @RequestParam long promotionId) {
        promotionService.setpromotion(bookId,promotionId);
    }
    @GetMapping("/promotion/deactive")
    public void deactivepromotions(@RequestParam long promotionId) {
        promotionService.deactivatePromotion(promotionId);
    }
    @DeleteMapping("/promotion/{id}")
    public ResponseEntity<?> deletepromotion(@PathVariable Long id) {
        Boolean aBoolean = promotionService.deleteBookPromotion(id);
        if(aBoolean.equals(Boolean.TRUE))
            return ResponseEntity.ok(true);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse>listofbooks(@PathVariable Integer id)
    {
        BookResponse bookResponse = bookService.findbyid(id);
        return ResponseEntity.ok(bookResponse);
    }
    @GetMapping("/genres")
    public ResponseEntity<List<GenreResponse>>listofgenres()
    {
        List<GenreResponse> genreResponse = bookService.findallgenres();
        return ResponseEntity.ok(genreResponse);
    }
    @GetMapping("/search/title")
    public ResponseEntity<Page<BookResponse>> listofbooksbytitle(@RequestParam String title,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size)
    {
        Page<BookResponse> bookResponse = bookService.findbooksbytitle(title,page,size);
        return ResponseEntity.ok(bookResponse);
    }
    @GetMapping("/search/genre")
    public ResponseEntity<Page<BookResponse>> listofbooksbygenre(@RequestParam String genre_name,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size)
    {
        Page<BookResponse> bookResponse = bookService.findbooksbygenre(genre_name,page,size);
        return ResponseEntity.ok(bookResponse);
    }
    @GetMapping("/search/publisher")
    public ResponseEntity<Page<BookResponse>> listofbooksbypublisher(@RequestParam String publisher_name,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size)
    {
        Page<BookResponse> bookResponses= bookService.findbooksbypublisher(publisher_name,page,size);
        return ResponseEntity.ok(bookResponses);
    }
    @GetMapping("/search/author")
    public ResponseEntity<Page<BookResponse>> listofbooksbyauthor(@RequestParam String name, @RequestParam String surname,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size)
    {
        Page<BookResponse> bookResponses = bookService.findbooksbyauthor(name, surname,page,size);
        return ResponseEntity.ok(bookResponses);
    }
    @GetMapping("/search/price")
    public ResponseEntity<Page<BookResponse>> listofbooksbyprice(@RequestParam float min, @RequestParam float max,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        Page<BookResponse> bookResponses = bookService.findbooksbyprice(min, max,page,size);
        return ResponseEntity.ok(bookResponses);
    }
    @GetMapping("/search/year")
    public ResponseEntity<Page<BookResponse>> listofbooksbyyear(@RequestParam Integer year1, @RequestParam Integer year2,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        Page<BookResponse> bookResponses = bookService.findbooksbyyear(year1, year2,page,size);
        return ResponseEntity.ok(bookResponses);
    }
    //////
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<BookRequest> addbook(@RequestBody BookRequest bookRequest) {
        BookRequest addedbook = bookService.addbook(bookRequest);
        return ResponseEntity.ok(addedbook);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<BookRequest> updatebook(@PathVariable Integer id,@RequestBody BookRequest bookRequest) {
        BookRequest updatedBook = bookService.updateBook(id,bookRequest);
        return ResponseEntity.ok(updatedBook);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            bookService.deletebook(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
