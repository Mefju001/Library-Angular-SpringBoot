package com.app.library.DTO.Mapper;

import com.app.library.DTO.Request.LibraryRequest;
import com.app.library.DTO.Response.LibraryResponse;
import com.app.library.Entity.Library;
import org.springframework.stereotype.Component;

@Component
public class LibraryMapper {
    public LibraryResponse toDto(Library library)
    {
        return new LibraryResponse(library.getId(), library.getAddress(),  library.getMap());
    }
    public void updateTheLibrary(Library library, LibraryRequest libraryRequest)
    {
        library.setAddress(libraryRequest.address());
        library.setMap(libraryRequest.map());
    }
}
