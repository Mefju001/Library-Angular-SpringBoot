package com.app.library.Service;

import com.app.library.DTO.Mapper.LibraryBookMapper;
import com.app.library.DTO.Mapper.LibraryMapper;
import com.app.library.DTO.Request.LibraryBookRequest;
import com.app.library.DTO.Request.LibraryRequest;
import com.app.library.DTO.Response.LibraryBookResponse;
import com.app.library.DTO.Response.LibraryResponse;
import com.app.library.Entity.Book;
import com.app.library.Entity.Library;
import com.app.library.Entity.LibraryBook;
import com.app.library.Repository.BookRepository;
import com.app.library.Repository.LibraryBookRepository;
import com.app.library.Repository.LibraryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {
    final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;
    private final LibraryBookRepository libraryBookRepository;
    private final LibraryMapper libraryMapper;
    private final LibraryBookMapper libraryBookMapper;
    @Autowired
    public LibraryService(BookRepository bookRepository, LibraryRepository libraryRepository, LibraryBookRepository libraryBookRepository, LibraryMapper libraryMapper, LibraryBookMapper libraryBookMapper) {
        this.bookRepository = bookRepository;
        this.libraryRepository = libraryRepository;
        this.libraryBookRepository = libraryBookRepository;
        this.libraryMapper = libraryMapper;
        this.libraryBookMapper = libraryBookMapper;
    }
    public List<LibraryResponse>findall()
    {
        List<Library> libraries = libraryRepository.findAll();
        return libraries.stream().map(libraryMapper::toDto).toList();
    }
    public LibraryResponse findbyid(Integer id)
    {
        Optional<Library> Optlibrary = libraryRepository.findById(id);
        if(Optlibrary.isPresent())
        {
            return Optlibrary.map(libraryMapper::toDto).orElseThrow();
        }
        else {
            throw new EntityNotFoundException("not found");
        }
    }
    public List<LibraryResponse>findlibrarybyname(String name)
    {
        List<Library> libraries = libraryRepository.findLibraryByName(name);
        return libraries.stream().map(libraryMapper::toDto).toList();
    }
    public List<LibraryBookResponse>findallbookandlibrary()
    {
        List<LibraryBook> libraries = libraryBookRepository.findAll();
        return libraries.stream().map(libraryBookMapper::toLibraryBookResponse).toList();
    }
    public List<LibraryBookResponse>findbookinlibraries(String title)
    {
        List<LibraryBook> libraries = libraryBookRepository.findLibraryBookByBook_Title(title);
        return libraries.stream().map(libraryBookMapper::toLibraryBookResponse).toList();
    }
    @Transactional
    public LibraryResponse addlibrary(LibraryRequest library)
    {
        Library savedLibrary = new Library();
        savedLibrary.setName(library.getName());
        savedLibrary.setAddress(library.getAddress());
        libraryRepository.save(savedLibrary);
        return new LibraryResponse(savedLibrary.getId(), savedLibrary.getName(), savedLibrary.getAddress());
    }
    LibraryBook setLibraryBook(LibraryBookRequest request)
    {
        Book book = bookRepository.findBookByIsbnIs(request.getIsbn());
        Library library = libraryRepository.findById(request.getIdLibrary()).orElseThrow();

        LibraryBook libraryBook = new LibraryBook();
        libraryBook.setBook(book);
        libraryBook.setLibrary(library);
        return libraryBook;
    }
    @Transactional
    public LibraryBookResponse addbooktolibrary(LibraryBookRequest request)
    {
        libraryBookRepository.save(setLibraryBook(request));

        return LibraryBookResponse.builder()
                .id(request.getId())
                .title(request.getTitle())
                .authorName(request.getAuthorName())
                .authorSurname(request.getAuthorSurname())
                .publicationYear(request.getPublicationYear())
                .isbn(request.getIsbn())
                .genreName(request.getGenreName())
                .language(request.getLanguage())
                .publisherName(request.getPublisherName())
                .pages(request.getPages())
                .price(request.getPrice())
                .idLibrary(request.getIdLibrary())
                .name(request.getName())
                .address(request.getAddress())
                .build();
    }

    @Transactional
    public LibraryResponse updatelibrary(Integer id,LibraryRequest library)
    {
        Optional<Library> existinglibrary = libraryRepository.findById(id);
        if(existinglibrary.isPresent()) {
            Library updatedlibrary = existinglibrary.get();
            updatedlibrary.setName(library.getName());
            updatedlibrary.setAddress(library.getAddress());
            libraryRepository.save(updatedlibrary);
            return new LibraryResponse(existinglibrary.get().getId(),existinglibrary.get().getName(),existinglibrary.get().getAddress());
        }
        else {
            throw new EntityNotFoundException("not found");
        }
    }
    @Transactional
    public LibraryBookResponse updatebookandlibrary(LibraryBookRequest libraryBookRequest)
    {
        Optional<LibraryBook> existingdata = libraryBookRepository.findById(libraryBookRequest.getId());
        if(existingdata.isPresent()) {
            libraryBookRepository.save(setLibraryBook(libraryBookRequest));
            return LibraryBookResponse.builder()
                    .id(libraryBookRequest.getId())
                    .title(libraryBookRequest.getTitle())
                    .authorName(libraryBookRequest.getAuthorName())
                    .authorSurname(libraryBookRequest.getAuthorSurname())
                    .publicationYear(libraryBookRequest.getPublicationYear())
                    .isbn(libraryBookRequest.getIsbn())
                    .genreName(libraryBookRequest.getGenreName())
                    .language(libraryBookRequest.getLanguage())
                    .publisherName(libraryBookRequest.getPublisherName())
                    .pages(libraryBookRequest.getPages())
                    .price(libraryBookRequest.getPrice())
                    .build();
        }
        else {
            throw new EntityNotFoundException("not found");
        }
    }
    @Transactional
    public void deletelibrary(Integer id)
    {
        if(!libraryRepository.existsById(id)) {
            throw new EntityNotFoundException("Library not found with id: " + id);
        }
        libraryRepository.deleteById(id);
    }
    @Transactional
    public void deletebookandlibrary(Integer id)
    {
        if(!libraryBookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book in library not found with id: " + id);
        }
        libraryBookRepository.deleteById(id);
    }
}
