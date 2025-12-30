package com.app.library;

import com.app.library.Builder.Book.BookBuilder;
import com.app.library.DTO.Mapper.BookMapper;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.Entity.Author;
import com.app.library.Entity.Book;
import com.app.library.Entity.Genre;
import com.app.library.Entity.Publisher;
import com.app.library.Repository.BookRepository;
import com.app.library.Service.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookBuilder bookBuilder;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;
    @Test
    void findBookById() {
        var isbn = 1234567890L;
        Book book = new Book(
                new Author(),
                "Władca Pierścieni",
                new Publisher(),
                LocalDate.now(),
                10.0f,
                500,
                null,
                "Polski",
                1234567890L,
                1,
                new Genre()
        );
        BookResponse bookResponse = new BookResponse(
                1,book.getTitle(),"null","null",LocalDate.now(),
                0L,"null","null","null",9,0f);
        when(bookRepository.findBookByIsbn(anyLong())).thenReturn(Optional.of(book));
        when(bookMapper.ToDto(book)).thenReturn(bookResponse);
        var actualResponse = bookService.findByIsbn(isbn);
        verify(bookRepository, times(1)).findBookByIsbn(isbn);
        assertNotNull(actualResponse);
        assertEquals(isbn, actualResponse.getIsbn());
        assertEquals("Władca Pierścieni", actualResponse.getTitle());
    }
    @Test
    void findAllBooksWhenResultIsEmpty() {
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());
        Page<BookResponse> result = bookService.findAll(0,10);
        assertTrue(result.isEmpty());
        verify(bookRepository).findAll(any(Pageable.class));
    }
    @Test
    void findAllBooksWhenResultIsNotEmpty() {
        List<Book> listOfBooks = new ArrayList<>();
        listOfBooks.add(new Book());
        listOfBooks.add(new Book());
        BookResponse bookResponse = new BookResponse(
                1,"book.getTitle()","null","null",LocalDate.now(),
                0L,"null","null","null",9,0f);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Book>books = new PageImpl<>(listOfBooks);
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(books);
        when(bookMapper.ToDto(any(Book.class))).thenReturn(bookResponse);
        var result = bookService.findAll(pageable.getPageNumber(), pageable.getPageSize());
        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals(2, result.getTotalElements());
        verify(bookRepository, times(1)).findAll(any(Pageable.class));
        verify(bookMapper, times(2)).ToDto(any(Book.class));

    }
}
