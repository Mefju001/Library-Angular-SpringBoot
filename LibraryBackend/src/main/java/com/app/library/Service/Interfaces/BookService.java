package com.app.library.Service.Interfaces;

import com.app.library.DTO.Request.BookCriteria;
import com.app.library.DTO.Request.BookRequest;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.Entity.Book;
import com.app.library.Entity.BookImg;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookService {
    Book getInternalBookEntity(Integer id);
    Book findByIsbn(Long isbn);
    Page<BookResponse> findAll(int page, int size);
    List<BookResponse> findAllList();
    BookResponse findById(Integer id);
    BookImg findBookImgById(Integer id);
    Long getNewBooksCount();
    BookResponse addbook(BookRequest bookRequest);
    BookResponse updateBook(Integer id, BookRequest bookRequest);
    void deletebook(Integer id);
    Page<BookResponse> searchOrSortBooksByCriteria(BookCriteria criteria);
}
