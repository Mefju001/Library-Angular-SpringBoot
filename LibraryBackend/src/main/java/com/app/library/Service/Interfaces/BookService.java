package com.app.library.Service.Interfaces;

import com.app.library.DTO.Request.BookCriteria;
import com.app.library.DTO.Request.BookRequest;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.DTO.Response.GenreResponse;
import com.app.library.Entity.BookImg;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookService {
    Page<BookResponse> findAll(int page, int size);

    List<BookResponse> findAllList();

    BookResponse findById(Integer id);

    BookImg findBookImgById(Integer id);

    List<GenreResponse> findallgenres();

    Long getNewBooksCount();

    BookRequest addbook(BookRequest bookRequest);

    BookRequest updateBook(Integer id, BookRequest bookRequest);

    void deletebook(Integer id);

    Page<BookResponse> searchOrSortBooksByCriteria(BookCriteria criteria);
}
