package com.app.library.Service;

import com.app.library.DTO.Mapper.LibraryMapper;
import com.app.library.DTO.MediatorRequest.AuditRequest;
import com.app.library.DTO.Request.LibraryRequest;
import com.app.library.DTO.Response.LibraryResponse;
import com.app.library.Entity.Library;
import com.app.library.Mediator.Mediator;
import com.app.library.Repository.LibraryRepository;
import com.app.library.Service.Interfaces.LibraryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryServiceImpl implements LibraryService {
    private final Mediator mediator;
    private final LibraryRepository libraryRepository;
    private final LibraryMapper libraryMapper;

    @Autowired
    public LibraryServiceImpl(LibraryRepository libraryRepository, LibraryMapper libraryMapper, Mediator mediator) {
        this.libraryRepository = libraryRepository;
        this.libraryMapper = libraryMapper;
        this.mediator = mediator;
    }
    private String currentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
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
            return Optlibrary.map(libraryMapper::toDto).orElseThrow(()-> new EntityNotFoundException("Library not found"));
        } else {
            throw new EntityNotFoundException("Library not found");
        }
    }

    @Override
    public Library findByAddress(String address) {
        var library = libraryRepository.findLibraryByAddressContainingIgnoreCase(address);
        if(library==null){
            throw new EntityNotFoundException("Library not found");
        }
        return library;
    }

    @Override
    @Transactional
    public LibraryResponse addlibrary(LibraryRequest library) {
        var newLibrary = new Library(library.address(),library.map());
        newLibrary = libraryRepository.save(newLibrary);
        mediator.send(new AuditRequest("Post","Library",currentUser(), LocalDateTime.now(), "Dodawanie biblioteki do bazy danych", newLibrary));
        return libraryMapper.toDto(newLibrary);
    }

    @Override
    @Transactional
    public LibraryResponse updatelibrary(Integer id, LibraryRequest library) {
        Optional<Library> existinglibrary = libraryRepository.findById(id);
        Library updatedlibrary = existinglibrary.orElseThrow(()-> new EntityNotFoundException("Library not found"));
        libraryMapper.updateTheLibrary(updatedlibrary, library);
        updatedlibrary = libraryRepository.save(updatedlibrary);
        mediator.send(new AuditRequest("Update","Library",currentUser(),LocalDateTime.now(),"Edycja istniejącej biblioteki w bazie danych",updatedlibrary));
        return libraryMapper.toDto(updatedlibrary);
    }

    @Override
    @Transactional
    public void deletelibrary(Integer id) {
        var optLibrary = libraryRepository.findById(id);
        var deletedLibrary = optLibrary.orElseThrow(()-> new EntityNotFoundException("Library not found"));
        libraryRepository.delete(deletedLibrary);
        mediator.send(new AuditRequest("Delete", "Library",currentUser(),LocalDateTime.now(),"Usuwanie biblioteki z bazy danych",deletedLibrary));
    }

}
