package com.app.library.Service;

import com.app.library.DTO.Mapper.LibraryBookMapper;
import com.app.library.DTO.Mapper.LibraryMapper;
import com.app.library.DTO.Request.LibraryRequest;
import com.app.library.DTO.Response.LibraryResponse;
import com.app.library.Entity.Library;
import com.app.library.Mediator.Mediator;
import com.app.library.Repository.LibraryBookRepository;
import com.app.library.Repository.LibraryRepository;
import com.app.library.Service.Interfaces.LibraryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryServiceImpl implements LibraryService {
    private final Mediator mediator;
    private final LibraryRepository libraryRepository;
    private final LibraryMapper libraryMapper;

    @Autowired
    public LibraryServiceImpl(LibraryRepository libraryRepository, LibraryBookRepository libraryBookRepository, LibraryMapper libraryMapper, LibraryBookMapper libraryBookMapper, Mediator mediator) {
        this.libraryRepository = libraryRepository;
        this.libraryMapper = libraryMapper;
        this.mediator = mediator;
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
    @Transactional
    public LibraryResponse addlibrary(LibraryRequest library) {
        Library savedLibrary = new Library();
        savedLibrary.setAddress(library.address());
        savedLibrary.setMap(library.map());
        libraryRepository.save(savedLibrary);
        return new LibraryResponse(savedLibrary.getId(), savedLibrary.getAddress(), savedLibrary.getMap());
    }


    @Override
    @Transactional
    public LibraryResponse updatelibrary(Integer id, LibraryRequest library) {
        Optional<Library> existinglibrary = libraryRepository.findById(id);
        if (existinglibrary.isPresent()) {
            Library updatedlibrary = existinglibrary.get();
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
    public void deletelibrary(Integer id) {
        if (!libraryRepository.existsById(id)) {
            throw new EntityNotFoundException("Library not found with id: " + id);
        }
        libraryRepository.deleteById(id);
    }

}
