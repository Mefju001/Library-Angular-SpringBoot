package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.LibraryBookResponse;
import com.app.library.Entity.LibraryBook;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "Spring", uses = {LibraryMapper.class, BookMapper.class})
public interface LibraryBookMapper {
    LibraryBookResponse toLibraryBookResponse(LibraryBook libraryBook);
}
