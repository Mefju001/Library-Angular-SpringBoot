package com.app.library;

import com.app.library.Builder.BookBuilder;
import com.app.library.DTO.Mapper.BookMapper;
import com.app.library.DTO.Request.BookRequest;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.Entity.Author;
import com.app.library.Entity.Book;
import com.app.library.Entity.Genre;
import com.app.library.Entity.Publisher;
import com.app.library.Repository.BookRepository;
import com.app.library.Service.BookServiceImpl;
import com.app.library.Service.Interfaces.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        var id = 1;
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
                id,book.getTitle(),"null","null",LocalDate.now(),
                0L,"null","null","null",9,0f);
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(bookMapper.ToBookResponse(book)).thenReturn(bookResponse);
        BookResponse actualResponse = bookService.findById(id);
        verify(bookRepository, times(1)).findById(id);

        assertNotNull(actualResponse);
        assertEquals(id, actualResponse.id());
        assertEquals("Władca Pierścieni", actualResponse.title());
    }
}
