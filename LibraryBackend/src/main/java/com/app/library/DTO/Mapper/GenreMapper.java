package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.GenreResponse;
import com.app.library.Entity.Genre;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {
    public GenreResponse toDto(Genre genre)
    {
        return new GenreResponse(genre.getName());
    }
}
