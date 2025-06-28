package com.app.library.DTO.Mapper;


import com.app.library.DTO.Response.BookResponse;
import com.app.library.Entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "Spring")
public interface BookMapper {

    @Mapping(source = "author.name", target = "authorName")
    @Mapping(source = "author.surname", target = "authorSurname")
    @Mapping(source = "genre.name", target = "genreName")
    @Mapping(source = "publisher.name", target = "publisherName")
    BookResponse toDto(Book book);
}
