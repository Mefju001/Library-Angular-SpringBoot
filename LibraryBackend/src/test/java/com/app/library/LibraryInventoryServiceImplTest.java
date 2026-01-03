package com.app.library;

import com.app.library.DTO.Mapper.BookMapper;
import com.app.library.DTO.Mapper.LibraryBookMapper;
import com.app.library.DTO.Mapper.LibraryMapper;
import com.app.library.DTO.MediatorRequest.AuditRequest;
import com.app.library.DTO.MediatorRequest.LibraryBookDetails;
import com.app.library.DTO.MediatorResponse.LibraryBookDetailsResponse;
import com.app.library.DTO.Request.BookRequest;
import com.app.library.DTO.Request.LibraryBookRequest;
import com.app.library.DTO.Request.LibraryRequest;
import com.app.library.Entity.*;
import com.app.library.Mediator.Interfaces.ICommand;
import com.app.library.Mediator.Interfaces.IRequest;
import com.app.library.Mediator.Mediator;
import com.app.library.Repository.LibraryBookRepository;
import com.app.library.Service.Interfaces.GenreService;
import com.app.library.Service.Interfaces.RelationalEntityService;
import com.app.library.Service.LibraryInventoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class LibraryInventoryServiceImplTest {
    @Mock
    private LibraryBookRepository libraryBookRepository;
    @Mock
    private Mediator mediator;
    @Spy
    private BookMapper bookMapper = new BookMapper();
    @Spy
    private LibraryMapper libraryMapper = new LibraryMapper();
    @Spy
    private LibraryBookMapper libraryBookMapper = new LibraryBookMapper(bookMapper, libraryMapper);
    @InjectMocks
    private LibraryInventoryServiceImpl libraryInventoryServiceImpl;
    @Mock
    private RelationalEntityService relationalEntityService;
    @Mock
    private GenreService genreService;

    private void setAuth() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(authentication.getName()).thenReturn("test-user");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    private BookRequest getBookRequest() {
        return new BookRequest(
                "Władca Pierścieni", "J.R.R", "Tolkien", LocalDate.now(),
                12345678912L, "Fantasy", "Polski", "Zysk i S-ka", 9, 20.11f);
    }

    private LibraryRequest getLibraryRequest() {
        return new LibraryRequest("Katowice", "...");
    }
    private LibraryBookDetailsResponse  getLibraryBookDetailsResponse() {
        var book = new Book(
                new Author("J.R.R","Tolkien"),
                "Władca Pierścieni",
                new Publisher("Zysk i S-ka"),
                LocalDate.now(),
                10.0f,
                500,
                10.0f,
                "Polski",
                12345678912L,
                1,
                new Genre("Fantasy")
        );
        var library = new Library(1,"Katowice","...");
        return new LibraryBookDetailsResponse(book,library);
    }
    private LibraryBook getLibraryBook() {
        var book = new Book(
                new Author("J.R.R","Tolkien"),
                "Władca Pierścieni",
                new Publisher("Zysk i S-ka"),
                LocalDate.now(),
                10.0f,
                500,
                10.0f,
                "Polski",
                12345678912L,
                1,
                new Genre("Fantasy")
        );
        var library = new Library(1,"Katowice","...");
        return new LibraryBook(1,book,library,100);
    }
    @Test
    @WithMockUser(username = "test-user")
    void Add() {
        var request = new LibraryBookRequest(getBookRequest(), getLibraryRequest());
        var stock = 100;
        var detailsResponse = getLibraryBookDetailsResponse();
        var libraryBook = getLibraryBook();
        setAuth();
        when(mediator.send(any(LibraryBookDetails.class))).thenReturn(detailsResponse);
        when(libraryBookRepository.save(any(LibraryBook.class))).thenReturn(libraryBook);
        ArgumentCaptor<AuditRequest> auditCaptor = ArgumentCaptor.forClass(AuditRequest.class);
        var result = libraryInventoryServiceImpl.Add(request, stock);
        verify(libraryBookRepository, times(1)).save(any(LibraryBook.class));
        verify(mediator, times(1)).send(any(IRequest.class));
        verify(mediator, times(1)).send(any(ICommand.class));
        verify(mediator).send(auditCaptor.capture());
        AuditRequest capturedRequest = auditCaptor.getValue();
        assertEquals("test-user", capturedRequest.performedBy());
        assertEquals("Post", capturedRequest.action());
        assertNotNull(result);
        assertEquals("Władca Pierścieni", result.book().title());
        SecurityContextHolder.clearContext();
    }
    @Test
    @WithMockUser(username = "test-user")
    void Update() {
        var request = new LibraryBookRequest(getBookRequest(), getLibraryRequest());
        var stock = 200;
        var detailsResponse = getLibraryBookDetailsResponse();
        var libraryBook = getLibraryBook();
        setAuth();
        when(libraryBookRepository.findById(anyInt())).thenReturn(Optional.of(libraryBook));
        when(mediator.send(any(LibraryBookDetails.class))).thenReturn(detailsResponse);
        when(libraryBookRepository.save(any(LibraryBook.class))).thenReturn(libraryBook);
        ArgumentCaptor<AuditRequest> auditCaptor = ArgumentCaptor.forClass(AuditRequest.class);
        var result = libraryInventoryServiceImpl.Update(1, request, stock);
        verify(libraryBookRepository, times(1)).save(any(LibraryBook.class));
        verify(libraryBookRepository, times(1)).findById(anyInt());
        verify(mediator, times(1)).send(any(IRequest.class));
        verify(mediator, times(1)).send(any(ICommand.class));
        verify(mediator).send(auditCaptor.capture());
        AuditRequest capturedRequest = auditCaptor.getValue();
        assertEquals("test-user", capturedRequest.performedBy());
        assertEquals("Update", capturedRequest.action());
        assertNotNull(result);
        assertEquals(stock, result.Stock());
        assertEquals("Władca Pierścieni", result.book().title());
        SecurityContextHolder.clearContext();
    }
    @Test
    @WithMockUser(username = "test-user")
    void Delete() {
        setAuth();
        var libraryBook = getLibraryBook();
        final int id = 1;
        when(libraryBookRepository.findById(anyInt())).thenReturn(Optional.of(libraryBook));
        ArgumentCaptor<AuditRequest> auditCaptor = ArgumentCaptor.forClass(AuditRequest.class);
        libraryInventoryServiceImpl.Delete(id);
        verify(libraryBookRepository, times(1)).findById(id);
        verify(libraryBookRepository, times(1)).delete(libraryBook);
        verify(mediator, times(1)).send(auditCaptor.capture());
        AuditRequest capturedRequest = auditCaptor.getValue();
        assertEquals("test-user", capturedRequest.performedBy());
        assertEquals("Delete", capturedRequest.action());
        assertEquals(libraryBook,capturedRequest.object());
        SecurityContextHolder.clearContext();
    }
    @Test
    void FindAll()
    {
        List<LibraryBook> bookList = new ArrayList<>();
        bookList.add(getLibraryBook());
        var libBook = getLibraryBook();
        libBook.setId(2);
        bookList.add(libBook);
        when(libraryBookRepository.findAll()).thenReturn(bookList);
        var results = libraryInventoryServiceImpl.findallbookandlibrary();
        verify(libraryBookRepository, times(1)).findAll();
        assertEquals(bookList.size(), results.size());
    }
    @Test
    void FindTitle()
    {
        List<LibraryBook> bookList = new ArrayList<>();
        bookList.add(getLibraryBook());
        var title = "Władca Pierścieni";
        when(libraryBookRepository.findLibraryBookByBook_Title(title)).thenReturn(bookList);
        var results = libraryInventoryServiceImpl.findbookByTitleInLibraries(title);
        verify(libraryBookRepository, times(1)).findLibraryBookByBook_Title(title);
        assertEquals(title, results.getFirst().book().title());
        assertEquals(bookList.size(), results.size());
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }
}
