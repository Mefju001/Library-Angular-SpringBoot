package com.app.library.Service;

import com.app.library.DTO.Mapper.LibraryBookMapper;
import com.app.library.DTO.MediatorRequest.AuditRequest;
import com.app.library.DTO.MediatorRequest.LibraryBookDetails;
import com.app.library.DTO.Request.LibraryBookRequest;
import com.app.library.DTO.Response.LibraryBookResponse;
import com.app.library.Entity.LibraryBook;
import com.app.library.Mediator.Mediator;
import com.app.library.Repository.LibraryBookRepository;
import com.app.library.Service.Interfaces.LibraryInventoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LibraryInventoryServiceImpl implements LibraryInventoryService {
    private final LibraryBookMapper libraryBookMapper;
    private final LibraryBookRepository libraryBookRepository;
    private final Mediator mediator;
    public LibraryInventoryServiceImpl(LibraryBookMapper libraryBookMapper, LibraryBookRepository libraryBookRepository, Mediator mediator) {
        this.libraryBookMapper = libraryBookMapper;
        this.libraryBookRepository = libraryBookRepository;
        this.mediator = mediator;
    }
    private String CurrentUser()
    {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    @Override
    @Transactional
    public LibraryBookResponse Add(LibraryBookRequest request,Integer Stock)
    {
        var libraryBookDetails = mediator.send(new LibraryBookDetails(request.book().isbn(),request.library().address()));
        var libraryBook =  new LibraryBook(libraryBookDetails.library(),libraryBookDetails.book());
        libraryBook.setStock(Stock);
        libraryBook = libraryBookRepository.save(libraryBook);
        mediator.send(new AuditRequest("Post","LibraryBook",CurrentUser(), LocalDateTime.now(),"Dodanie dostępności ksiazki w danej bibliotece",libraryBook));
        return libraryBookMapper.toDto(libraryBook);
    }

    @Override
    @Transactional
    public LibraryBookResponse Update(int id, LibraryBookRequest libraryBookRequest,Integer stock) {
        var libraryBook = libraryBookRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        var libraryBookDetails = mediator.send(new LibraryBookDetails(libraryBookRequest.book().isbn(),libraryBookRequest.library().address()));
        libraryBookMapper.UpdateLibraryBook(libraryBook,libraryBookDetails.library(),libraryBookDetails.book(),stock);
        libraryBook = libraryBookRepository.save(libraryBook);
        return libraryBookMapper.toDto(libraryBook);
    }

    @Override
    @Transactional
    public void Delete(Integer id) {
        var libraryBook = libraryBookRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        libraryBookRepository.delete(libraryBook);
        mediator.send(new AuditRequest("Delete", "LibraryBook", CurrentUser(), LocalDateTime.now(), "Usunięcie dostępności książki z biblioteki", libraryBook));
    }

    @Override
    public List<LibraryBookResponse> findallbookandlibrary() {
        var libraryBooks = libraryBookRepository.findAll();
        return libraryBooks.stream().map(libraryBookMapper::toDto).toList();
    }

    @Override
    public List<LibraryBookResponse> findbookByTitleInLibraries(String title) {
        var libraryBook = libraryBookRepository.findLibraryBookByBook_Title(title);
        return libraryBook.stream().map(libraryBookMapper::toDto).toList();
    }
}
