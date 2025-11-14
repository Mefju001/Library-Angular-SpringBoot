package com.app.library.DTO.Mapper;


import com.app.library.DTO.Response.BookResponse;
import com.app.library.Entity.Book;


public class BookMapper {
    public static BookResponse ToBookResponse(Book book) {
       return new  BookResponse(
               book.getId(),
               book.getTitle(),
               book.getAuthor().getName(),
               book.getAuthor().getSurname(),
               book.getpublicationDate(),
               book.getIsbn(),
               book.getGenre().getName(),
               book.getLanguage(),
               book.getPublisher().getName(),
               book.getPages(),
               book.getPrice()
       );
    }
}
