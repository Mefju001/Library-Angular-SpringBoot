package com.app.library;

import com.app.library.Builder.Book.BookBuilder;
import com.app.library.DTO.Mapper.BookMapper;
import com.app.library.DTO.Mapper.LibraryMapper;
import com.app.library.DTO.MediatorRequest.AuditRequest;
import com.app.library.DTO.Request.LibraryRequest;
import com.app.library.Entity.Library;
import com.app.library.Mediator.Mediator;
import com.app.library.Repository.BookRepository;
import com.app.library.Repository.LibraryRepository;
import com.app.library.Service.BookServiceImpl;
import com.app.library.Service.Interfaces.GenreService;
import com.app.library.Service.Interfaces.RelationalEntityService;
import com.app.library.Service.LibraryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceImplTest {
    @Mock
    private LibraryRepository libraryRepository;
    @Mock
    private Mediator mediator;
    @Spy
    private LibraryMapper libraryMapper = new LibraryMapper();
    @InjectMocks
    private LibraryServiceImpl libraryServiceImpl;
    @Captor
    private ArgumentCaptor<AuditRequest> auditCaptor;

    private Library getLibrary() {
        return new Library(1,"Katowice","...");
    }
    private LibraryRequest getLibraryRequest() {
        return new LibraryRequest("Katowice","...");
    }
    private void setAuth()
    {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(authentication.getName()).thenReturn("test-user");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
    @Test
    void findAllLibrary() {
        List<Library> libraryList = new ArrayList<>();
        libraryList.add(getLibrary());
        libraryList.add(getLibrary());
        when(libraryRepository.findAll()).thenReturn(libraryList);
        var results = libraryServiceImpl.findall();
        verify(libraryRepository).findAll();
        assertEquals(libraryList.size(), results.size());
    }
    @Test
    void findLibraryById() {
        final int id = 1;
        Library library = getLibrary();
        when(libraryRepository.findById(id)).thenReturn(Optional.of(library));
        var results = libraryServiceImpl.findbyid(id);
        verify(libraryRepository).findById(id);
        assertEquals(id,results.id());
        assertEquals(library.getAddress(), results.address());
        assertEquals(library.getMap(), results.map());
    }
    @Test
    void findLibraryByAddress() {
        Library library = getLibrary();
        final String address = "Katowice";
        when(libraryRepository.findLibraryByAddressContainingIgnoreCase(address)).thenReturn(library);
        var results = libraryServiceImpl.findByAddress(address);
        verify(libraryRepository).findLibraryByAddressContainingIgnoreCase(address);
        assertEquals(library,results);
    }
    @Test
    @WithMockUser(username = "test-user")
    void AddLibrary() {
        setAuth();
        var libraryRequest = getLibraryRequest();
        var library = getLibrary();
        when(libraryRepository.save(any(Library.class))).thenReturn(library);
        var result = libraryServiceImpl.addlibrary(libraryRequest);
        verify(libraryRepository).save(any(Library.class));
        assertEquals(library.getMap(),result.map());
        assertEquals(library.getAddress(),result.address());
        assertEquals(library.getId(),result.id());
        verify(mediator, times(1)).send(auditCaptor.capture());
        AuditRequest capturedRequest = auditCaptor.getValue();
        assertEquals("test-user", capturedRequest.performedBy());
        assertEquals("Post", capturedRequest.action());
        assertEquals("Library", capturedRequest.entity());
        assertEquals(library, capturedRequest.object());
        assertNotNull(capturedRequest.timestamp());
        SecurityContextHolder.clearContext();
    }
    @Test
    @WithMockUser(username = "test-user")
    void UpdateLibrary() {
        setAuth();
        final int id = 1;
        var library = getLibrary();
        library.setAddress("Gliwice");
        var libraryRequest = getLibraryRequest();
        when(libraryRepository.findById(id)).thenReturn(Optional.of(library));
        when(libraryRepository.save(any(Library.class))).thenReturn(library);
        var result = libraryServiceImpl.updatelibrary(id,libraryRequest);
        verify(libraryRepository).save(any(Library.class));
        assertEquals("Katowice",result.address());
        assertEquals(library.getId(),result.id());
        verify(mediator, times(1)).send(auditCaptor.capture());
        AuditRequest capturedRequest = auditCaptor.getValue();
        assertEquals("test-user", capturedRequest.performedBy());
        assertEquals("Update", capturedRequest.action());
        assertEquals("Library", capturedRequest.entity());
        assertEquals(library, capturedRequest.object());
        assertNotNull(capturedRequest.timestamp());
        SecurityContextHolder.clearContext();
    }
    @Test
    @WithMockUser(username = "test-user")
    void DeleteLibrary() {
        setAuth();
        final int id = 1;
        var library = getLibrary();
        when(libraryRepository.findById(id)).thenReturn(Optional.of(library));
        libraryServiceImpl.deletelibrary(id);
        verify(libraryRepository).delete(any(Library.class));
        verify(mediator, times(1)).send(auditCaptor.capture());
        AuditRequest capturedRequest = auditCaptor.getValue();
        assertEquals("test-user", capturedRequest.performedBy());
        assertEquals("Delete", capturedRequest.action());
        assertEquals("Library", capturedRequest.entity());
        assertEquals(library, capturedRequest.object());
        assertNotNull(capturedRequest.timestamp());
        SecurityContextHolder.clearContext();
    }
}
