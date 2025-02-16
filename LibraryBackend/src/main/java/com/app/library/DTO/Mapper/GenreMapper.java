package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.GenreResponse;
import com.app.library.Entity.Genre;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreResponse toDto(Genre genre);
}
