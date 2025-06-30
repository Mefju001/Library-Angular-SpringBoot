package com.app.library.Service;

import com.app.library.DTO.Request.BookRequest;
import com.app.library.DTO.Request.BookSearchCriteria;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.DTO.Response.GenreResponse;
import com.app.library.Entity.BookImg;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface BookService {
    Page<BookResponse> findall(int page, int size);

    List<BookResponse> findAllList();

    BookResponse findbyid(Integer id);

    BookImg findByBookId(Integer id);

    List<GenreResponse> findallgenres();

    Long getNewBooksCount();

    Page<BookResponse>sortBooks(int page, int size, String sortBy, String direction);

    BookRequest addbook(BookRequest bookRequest);

    BookRequest updateBook(Integer id, BookRequest bookRequest);

    void deletebook(Integer id);

    Page<BookResponse> searchBooks(BookSearchCriteria criteria);
}
