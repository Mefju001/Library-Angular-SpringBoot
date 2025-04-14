package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.BookResponse;
import com.app.library.DTO.Response.FavoriteBooksResponse;
import com.app.library.Entity.Book;
import com.app.library.Entity.Favoritebooks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "Spring")
public interface FavoriteBooksMapper {
    @Mapping(source = "book.title", target = "title")
    @Mapping(source = "book.author.name", target = "authorName")
    @Mapping(source = "book.author.surname", target = "authorSurname")
    @Mapping(source = "book.genre.name", target = "genreName")
    @Mapping(source = "book.publisher.name", target = "publisherName")
    @Mapping(source = "book.publicationDate", target = "publicationDate")
    @Mapping(source = "book.isbn", target = "isbn")
    @Mapping(source = "book.language", target = "language")
    @Mapping(source = "book.pages", target = "pages")
    @Mapping(source = "book.price", target = "price")
    FavoriteBooksResponse toDto(Favoritebooks favoritebooks);
}
