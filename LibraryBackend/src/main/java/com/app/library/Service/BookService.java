package com.app.library.Service;

import com.app.library.DTO.Request.BookRequest;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.DTO.Response.GenreResponse;
import com.app.library.Entity.BookImg;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface BookService {
    Page<BookResponse> findall(int page, int size);
    BookResponse findbyid(Integer id);
    BookImg findByBookId(Integer id);
    List<GenreResponse> findallgenres();
    Page<BookResponse> findbooksbygenre(String name, int page, int size);
    Page<BookResponse> findbooksbypublisher(String name, int page, int size);
    Page<BookResponse> findbooksbytitle(String title, int page, int size);
    Page<BookResponse> findbooksbyauthor(String name, String surname, int page, int size);
    Page<BookResponse> findbooksbyprice(Float min, Float max, int page, int size);
    Page<BookResponse> findbooksbyyear(LocalDate year1, LocalDate year2, int page, int size);
    Page<BookResponse> findnewbooks(int page, int size);
    Long getNewBooksCount();
    Page<BookResponse> findforeshadowedbooks(int page, int size);
    Page<BookResponse> sortbooktitle(int page, int size, String type);
    Page<BookResponse> sortbookprice(int page, int size, String type);
    Page<BookResponse> sortbookyear(int page, int size, String type);
    BookRequest addbook(BookRequest bookRequest);
    BookRequest updateBook(Integer id, BookRequest bookRequest);
    void deletebook(Integer id);
}
