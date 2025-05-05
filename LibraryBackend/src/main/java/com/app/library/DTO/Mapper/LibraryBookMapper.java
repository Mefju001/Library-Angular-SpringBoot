package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.LibraryBookResponse;
import com.app.library.Entity.LibraryBook;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "Spring")
public interface LibraryBookMapper {
    @Mapping(source = "book.id", target = "id")
    @Mapping(source = "book.title", target = "title")
    @Mapping(source = "book.author.name", target = "authorName")
    @Mapping(source = "book.author.surname", target = "authorSurname")
    @Mapping(source = "book.publicationDate", target = "publicationDate")
    @Mapping(source = "book.isbn", target = "isbn")
    @Mapping(source = "book.genre.name", target = "genreName")
    @Mapping(source = "book.language", target = "language")
    @Mapping(source = "book.publisher.name", target = "publisherName")
    @Mapping(source = "book.pages", target = "pages")
    @Mapping(source = "book.price", target = "price")
    @Mapping(source = "library.id", target = "idLibrary")
    @Mapping(source = "library.location", target = "location")
    @Mapping(source = "library.address", target = "address")
    @Mapping(source = "library.map", target = "map")
    LibraryBookResponse toLibraryBookResponse(LibraryBook libraryBook);
}
