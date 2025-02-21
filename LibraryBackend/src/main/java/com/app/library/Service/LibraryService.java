package com.app.library.Service;

import com.app.library.DTO.Mapper.LibraryBookMapper;
import com.app.library.DTO.Mapper.LibraryMapper;
import com.app.library.DTO.Response.LibraryBookResponse;
import com.app.library.DTO.Response.LibraryResponse;
import com.app.library.Entity.Library;
import com.app.library.Entity.LibraryBook;
import com.app.library.Repository.LibraryBookRepository;
import com.app.library.Repository.LibraryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {
    private final LibraryRepository libraryRepository;
    private final LibraryBookRepository libraryBookRepository;
    private final LibraryMapper libraryMapper;
    private final LibraryBookMapper libraryBookMapper;
    @Autowired
    public LibraryService(LibraryRepository libraryRepository, LibraryBookRepository libraryBookRepository, LibraryMapper libraryMapper, LibraryBookMapper libraryBookMapper) {
        this.libraryRepository = libraryRepository;
        this.libraryBookRepository = libraryBookRepository;
        this.libraryMapper = libraryMapper;
        this.libraryBookMapper = libraryBookMapper;
    }
    public ResponseEntity<List<LibraryResponse>>findall()
    {
        List<Library> libraries = libraryRepository.findAll();
        List<LibraryResponse>libraryResponses = libraries.stream().map(libraryMapper::toDto).toList();
        return new ResponseEntity<>(libraryResponses, HttpStatus.OK);
    }
    public ResponseEntity<List<LibraryResponse>>findlibrarybyname(String name)
    {
        List<Library> libraries = libraryRepository.findLibraryByName(name);
        List<LibraryResponse>libraryResponses = libraries.stream().map(libraryMapper::toDto).toList();
        return new ResponseEntity<>(libraryResponses, HttpStatus.OK);
    }
    public ResponseEntity<List<LibraryBookResponse>>findallbookandlibrary()
    {
        List<LibraryBook> libraries = libraryBookRepository.findAll();
        List<LibraryBookResponse>libraryBookResponses = libraries.stream().map(libraryBookMapper::toLibraryBookResponse).toList();
        return new ResponseEntity<>(libraryBookResponses, HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<Library>addlibrary(Library library)
    {
        libraryRepository.save(library);
        return new ResponseEntity<>(library,HttpStatus.CREATED);
    }
    @Transactional
    public ResponseEntity<LibraryBook>addbooktolibrary(LibraryBook LibraryBook)
    {
        libraryBookRepository.save(LibraryBook);
        return new ResponseEntity<>(LibraryBook,HttpStatus.CREATED);
    }
    @Transactional
    public ResponseEntity<Library>updatelibrary(Library library)
    {
        Optional<Library> existinglibrary = libraryRepository.findById(library.getId());
        if(existinglibrary.isPresent()) {
            Library updatedlibrary = existinglibrary.get();
            updatedlibrary.setName(library.getName());
            updatedlibrary.setAddress(library.getAddress());
            libraryRepository.save(updatedlibrary);
            return new ResponseEntity<>(library, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @Transactional
    public ResponseEntity<LibraryBook>updatebookandlibrary(LibraryBook libraryBook)
    {
        Optional<LibraryBook> existingdata = libraryBookRepository.findById(libraryBook.getId());
        if(existingdata.isPresent()) {
            LibraryBook updatedlibrary = existingdata.get();
            updatedlibrary.setBook(existingdata.get().getBook());
            updatedlibrary.setLibrary(existingdata.get().getLibrary());
            libraryBookRepository.save(updatedlibrary);
            return new ResponseEntity<>(updatedlibrary, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @Transactional
    public ResponseEntity<Library>deletelibrary(Integer id)
    {
        Optional<Library> existinglibrary = libraryRepository.findById(id);
        if(existinglibrary.isPresent()) {
            libraryRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @Transactional
    public ResponseEntity<LibraryBook>deletebookandlibrary(Integer id)
    {
        Optional<LibraryBook> existingdata = libraryBookRepository.findById(id);
        if(existingdata.isPresent()) {
            libraryBookRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
