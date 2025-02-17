package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.LibraryResponse;
import com.app.library.Entity.Library;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface LibraryMapper {
    LibraryResponse toDto(Library library);
}
