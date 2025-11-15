package com.app.library.Service;

import com.app.library.DTO.Mapper.LibraryBookMapper;
import com.app.library.DTO.Mapper.LibraryMapper;
import com.app.library.DTO.Request.LibraryBookRequest;
import com.app.library.DTO.Request.LibraryRequest;
import com.app.library.DTO.Response.LibraryBookResponse;
import com.app.library.DTO.Response.LibraryResponse;
import com.app.library.Entity.Library;
import com.app.library.Entity.LibraryBook;
import com.app.library.Repository.BookRepository;
import com.app.library.Repository.LibraryBookRepository;
import com.app.library.Repository.LibraryRepository;
import com.app.library.Service.Interfaces.LibraryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryServiceImpl implements LibraryService {
    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;
    private final LibraryBookRepository libraryBookRepository;
    private final LibraryMapper libraryMapper;
    private final LibraryBookMapper libraryBookMapper;

    @Autowired
    public LibraryServiceImpl(BookRepository bookRepository, LibraryRepository libraryRepository, LibraryBookRepository libraryBookRepository, LibraryMapper libraryMapper, LibraryBookMapper libraryBookMapper) {
        this.bookRepository = bookRepository;
        this.libraryRepository = libraryRepository;
        this.libraryBookRepository = libraryBookRepository;
        this.libraryMapper = libraryMapper;
        this.libraryBookMapper = libraryBookMapper;
    }

    @Override
    public List<LibraryResponse> findall() {
        List<Library> libraries = libraryRepository.findAll();
        return libraries.stream().map(libraryMapper::toDto).toList();
    }

    @Override
    public LibraryResponse findbyid(Integer id) {
        Optional<Library> Optlibrary = libraryRepository.findById(id);
        if (Optlibrary.isPresent()) {
            return Optlibrary.map(libraryMapper::toDto).orElseThrow();
        } else {
            throw new EntityNotFoundException("not found");
        }
    }

    @Override
    public List<LibraryResponse> findlibrarybylocation(String location) {
        List<Library> libraries = libraryRepository.findLibraryByLocation(location);
        return libraries.stream().map(libraryMapper::toDto).toList();
    }

    @Override
    public List<LibraryBookResponse> findallbookandlibrary() {
        List<LibraryBook> libraries = libraryBookRepository.findAll();
        return libraries.stream().map(libraryBookMapper::toLibraryBookResponse).toList();
    }

    @Override
    public List<LibraryBookResponse> findbookinlibraries(String title) {
        List<LibraryBook> libraries = libraryBookRepository.findLibraryBookByBook_Title(title);
        return libraries.stream().map(libraryBookMapper::toLibraryBookResponse).toList();
    }

    @Override
    @Transactional
    public LibraryResponse addlibrary(LibraryRequest library) {
        Library savedLibrary = new Library();
        savedLibrary.setLocation(library.location());
        savedLibrary.setAddress(library.address());
        savedLibrary.setMap(library.map());
        libraryRepository.save(savedLibrary);
        return new LibraryResponse(savedLibrary.getId(), savedLibrary.getLocation(), savedLibrary.getAddress(), savedLibrary.getMap());
    }

    LibraryBook setLibraryBook(LibraryBookRequest request) {
        var book = bookRepository.existsBooksByIsbn(request.book().isbn());
        var library = libraryRepository.findLibraryByLocationContainingIgnoreCaseAndAddressContainsIgnoreCase(request.library().location(), request.library().address());
        if (!book || library == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        var existBook = bookRepository.findBookByIsbn(request.book().isbn());
        LibraryBook libraryBook = new LibraryBook();
        libraryBook.setBook(existBook);
        libraryBook.setLibrary(library);
        return libraryBook;
    }

    @Override
    @Transactional
    public LibraryBookResponse addbooktolibrary(LibraryBookRequest request) {
        LibraryBook libraryBook = libraryBookRepository.save(setLibraryBook(request));
        LibraryBookResponse response = libraryBookMapper.toLibraryBookResponse(libraryBook);
        return response;
    }

    @Override
    @Transactional
    public LibraryResponse updatelibrary(Integer id, LibraryRequest library) {
        Optional<Library> existinglibrary = libraryRepository.findById(id);
        if (existinglibrary.isPresent()) {
            Library updatedlibrary = existinglibrary.get();
            updatedlibrary.setLocation(library.location());
            updatedlibrary.setAddress(library.address());
            updatedlibrary.setMap(library.map());
            Library saved = libraryRepository.save(updatedlibrary);
            LibraryResponse response = libraryMapper.toDto(saved);
            return response;
        } else {
            throw new EntityNotFoundException("not found");
        }
    }

    @Override
    @Transactional
    public LibraryBookResponse updatebookandlibrary(int id, LibraryBookRequest libraryBookRequest) {
        Optional<LibraryBook> existingdata = libraryBookRepository.findById(id);
        if (existingdata.isPresent()) {
            LibraryBook libraryBook = libraryBookRepository.save(setLibraryBook(libraryBookRequest));
            LibraryBookResponse response = libraryBookMapper.toLibraryBookResponse(libraryBook);
            return response;
        } else {
            throw new EntityNotFoundException("not found");
        }
    }

    @Override
    @Transactional
    public void deletelibrary(Integer id) {
        if (!libraryRepository.existsById(id)) {
            throw new EntityNotFoundException("Library not found with id: " + id);
        }
        libraryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deletebookandlibrary(Integer id) {
        if (!libraryBookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book in library not found with id: " + id);
        }
        libraryBookRepository.deleteById(id);
    }
}
