package com.app.library.Service;

import com.app.library.DTO.Mapper.LibraryMapper;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.DTO.Response.LibraryResponse;
import com.app.library.Entity.Library;
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
    private final LibraryMapper libraryMapper;
    @Autowired
    public LibraryService(LibraryRepository libraryRepository, LibraryMapper libraryMapper) {
        this.libraryRepository = libraryRepository;
        this.libraryMapper = libraryMapper;
    }
    public ResponseEntity<List<Library>>findall()
    {
        List<Library> libraries = libraryRepository.findAll();
        List<LibraryResponse>bookResponses = libraries.stream().map(libraryMapper::toDto).toList();
        return new ResponseEntity<>(libraries, HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<Library>addlibrary(Library library)
    {
        libraryRepository.save(library);
        return new ResponseEntity<>(library,HttpStatus.CREATED);
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
    public ResponseEntity<Library>deletelibrary(Integer id)
    {
        Optional<Library> existinglibrary = libraryRepository.findById(id);
        if(existinglibrary.isPresent()) {
            libraryRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
