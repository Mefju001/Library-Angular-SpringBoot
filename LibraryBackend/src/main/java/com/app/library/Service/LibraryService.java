package com.app.library.Service;

import com.app.library.DTO.Mapper.LibraryBookMapper;
import com.app.library.DTO.Mapper.LibraryMapper;
import com.app.library.DTO.Response.LibraryBookResponse;
import com.app.library.DTO.Response.LibraryResponse;
import com.app.library.Entity.Library;
import com.app.library.Entity.LibraryBook;
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
    @Transactional
    public Library addlibrary(Library library)
    {
        Library savedLibrary = new Library();
        savedLibrary.setName(library.getName());
        savedLibrary.setAddress(library.getAddress());
        libraryRepository.save(savedLibrary);
        return savedLibrary;
    }
    @Transactional
    public LibraryBook addbooktolibrary(LibraryBook LibraryBook)
    {
        libraryBookRepository.save(LibraryBook);
        return LibraryBook;
    }
    @Transactional
    public Library updatelibrary(Integer id,Library library)
    {
        Optional<Library> existinglibrary = libraryRepository.findById(id);
        if(existinglibrary.isPresent()) {
            Library updatedlibrary = existinglibrary.get();
            updatedlibrary.setName(library.getName());
            updatedlibrary.setAddress(library.getAddress());
            libraryRepository.save(updatedlibrary);
            return library;
        }
        else {
            throw new EntityNotFoundException("not found");
        }
    }
    @Transactional
    public LibraryBook updatebookandlibrary(LibraryBook libraryBook)
    {
        Optional<LibraryBook> existingdata = libraryBookRepository.findById(libraryBook.getId());
        if(existingdata.isPresent()) {
            LibraryBook updatedlibrary = existingdata.get();
            updatedlibrary.setBook(existingdata.get().getBook());
            updatedlibrary.setLibrary(existingdata.get().getLibrary());
            libraryBookRepository.save(updatedlibrary);
            return updatedlibrary;
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
