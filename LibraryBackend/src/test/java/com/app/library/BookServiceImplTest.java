package com.app.library;

import com.app.library.Builder.Book.BookBuilder;
import com.app.library.DTO.Mapper.BookMapper;
import com.app.library.DTO.Mapper.GenreMapper;
import com.app.library.DTO.MediatorRequest.AuditRequest;
import com.app.library.DTO.Request.BookRequest;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.Entity.Author;
import com.app.library.Entity.Book;
import com.app.library.Entity.Genre;
import com.app.library.Entity.Publisher;
import com.app.library.Mediator.Mediator;
import com.app.library.Repository.BookRepository;
import com.app.library.Service.BookServiceImpl;
import com.app.library.Service.Interfaces.GenreService;
import com.app.library.Service.Interfaces.RelationalEntityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private Mediator mediator;
    @Mock
    private BookBuilder bookBuilder;
    @Spy
    private BookMapper bookMapper = new BookMapper();
    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private RelationalEntityService relationalEntityService;
    @Mock
    private GenreService  genreService;
    private Book getBook(){
        return new Book(
                new Author("J.R.R","Tolkien"),
                "Władca Pierścieni",
                new Publisher("Zysk i S-ka"),
                LocalDate.now(),
                10.0f,
                500,
                10.0f,
                "Polski",
                1234567890L,
                1,
                new Genre("Fantasy")
        );
    }
    private BookResponse getBookResponse(){
        return new BookResponse(
                1,
                "Władca Pierścieni",
                "J.R.R",
                "Tolkien",
                LocalDate.now(),
                1234567890L,
                "Fantasy",
                "Polski",
                "Zysk i S-ka",
                500,
                10.0f);
    }
    private final void setAuth()
    {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(authentication.getName()).thenReturn("test-user");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
    @Test
    void findBookByIsbn() {
        final var isbn = 1234567890L;
        var book = getBook();
        when(bookRepository.findBookByIsbn(anyLong())).thenReturn(Optional.of(book));
        var actualResponse = bookService.findByIsbn(isbn);
        verify(bookRepository, times(1)).findBookByIsbn(isbn);
        assertNotNull(actualResponse);
        assertEquals(isbn, actualResponse.getIsbn());
        assertEquals("Władca Pierścieni", actualResponse.getTitle());
    }
    @Test
    void findBookByID() {
        final var id = 1;
        var book = getBook();
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        var actualResponse = bookService.findById(id);
        verify(bookRepository, times(1)).findById(id);
        assertNotNull(actualResponse);
        assertEquals("Władca Pierścieni", actualResponse.title());
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
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book>books = new PageImpl<>(listOfBooks);
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(books);
        var result = bookService.findAll(pageable.getPageNumber(), pageable.getPageSize());
        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals(2, result.getTotalElements());
        verify(bookRepository, times(1)).findAll(any(Pageable.class));
        verify(bookMapper, times(2)).ToDto(any(Book.class));
    }
    @Test
    @WithMockUser(username = "test-user")
    void deleteBookById() {
        setAuth();
        final int id = 1;
        Book book = new Book();
        book.setTitle("Władca Pierścieni");
        book.setIsbn(1234567890L);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        ArgumentCaptor<AuditRequest> auditCaptor = ArgumentCaptor.forClass(AuditRequest.class);
        bookService.deletebook(id);
        verify(bookRepository, times(1)).findById(id);
        verify(bookRepository, times(1)).delete(book);
        verify(mediator, times(1)).send(auditCaptor.capture());
        AuditRequest capturedRequest = auditCaptor.getValue();
        assertEquals("test-user", capturedRequest.performedBy());
        assertEquals("Delete", capturedRequest.action());
        assertEquals("Book", capturedRequest.entity());
        assertEquals(book, capturedRequest.object());
        assertNotNull(capturedRequest.timestamp());
        SecurityContextHolder.clearContext();
    }
    @Test
    @WithMockUser(username = "test-user")
    void addBook() {
        var bookRequest = new BookRequest(
                "Władca Pierścieni","null","null",LocalDate.now(),
                0L,"null","null","null",9,0f);
        setAuth();
        Book book = new Book();
        book.setTitle("Władca Pierścieni");
        book.setIsbn(1234567890L);
        BookResponse bookResponse = new BookResponse(
                1,book.getTitle(),"null","null",LocalDate.now(),
                0L,"null","null","null",9,0f);
        ArgumentCaptor<AuditRequest> auditCaptor = ArgumentCaptor.forClass(AuditRequest.class);
        when(bookRepository.existsBooksByIsbn(anyLong())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.ToDto(book)).thenReturn(bookResponse);
        when(relationalEntityService.getOrCreateAuthor(anyString(), anyString())).thenReturn(new Author());
        when(relationalEntityService.getOrCreatePublisher(anyString())).thenReturn(new Publisher());
        when(genreService.getOrCreateGenre(anyString())).thenReturn(new Genre());
        when(bookBuilder.CreateNewBook(anyString(),anyLong(),anyFloat())).thenReturn(bookBuilder);
        when(bookBuilder.WithAuthor(any(Author.class))).thenReturn(bookBuilder);
        when(bookBuilder.WithDetails(any(LocalDate.class),anyString(),anyInt())).thenReturn(bookBuilder);
        when(bookBuilder.WithGenre(any(Genre.class))).thenReturn(bookBuilder);
        when(bookBuilder.WithPublisher(any(Publisher.class))).thenReturn(bookBuilder);
        when(bookBuilder.build()).thenReturn(book);
        var result = bookService.addbook(bookRequest);
        verify(bookRepository, times(1)).existsBooksByIsbn(anyLong());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(mediator, times(1)).send(auditCaptor.capture());
        AuditRequest capturedRequest = auditCaptor.getValue();
        assertEquals("Władca Pierścieni",result.title());
        assertEquals("test-user", capturedRequest.performedBy());
        assertEquals("Post", capturedRequest.action());
        assertEquals("Book", capturedRequest.entity());
        assertEquals(book, capturedRequest.object());
        assertNotNull(capturedRequest.timestamp());
        SecurityContextHolder.clearContext();
    }
    @Test
    @WithMockUser
    void updateBook() {
        var bookRequest = new BookRequest(
                "Władca Pierścieni","J.R.R","Tolkien",LocalDate.now(),
                12345678912L,"Fantasy","Polski","Zysk i S-ka",9,20.11f);
        setAuth();
        Book book = new Book();
        book.setId(1);
        book.setTitle("nothing");
        BookResponse bookResponse = new BookResponse(
                1,"Władca Pierścieni","J.R.R","Tolkien",LocalDate.now(),
                12345678912L,"Fantasy","Polski","Zysk i S-ka",9,20.11f);
        ArgumentCaptor<AuditRequest> auditCaptor = ArgumentCaptor.forClass(AuditRequest.class);
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(genreService.getOrCreateGenre(anyString())).thenReturn(new Genre());
        when(relationalEntityService.getOrCreateAuthor(anyString(), anyString())).thenReturn(new Author());
        when(relationalEntityService.getOrCreatePublisher(anyString())).thenReturn(new Publisher());
        var result = bookService.updateBook(1,bookRequest);
        verify(bookRepository, times(1)).findById(anyInt());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(mediator, times(1)).send(auditCaptor.capture());
        AuditRequest capturedRequest = auditCaptor.getValue();
        assertEquals("Władca Pierścieni",result.title());
        assertEquals("test-user", capturedRequest.performedBy());
        assertEquals("Update", capturedRequest.action());
        assertEquals("Book", capturedRequest.entity());
        assertEquals(book, capturedRequest.object());
        assertNotNull(capturedRequest.timestamp());
        SecurityContextHolder.clearContext();
    }
}
