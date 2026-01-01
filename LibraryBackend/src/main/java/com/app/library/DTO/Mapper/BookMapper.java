package com.app.library.DTO.Mapper;


import com.app.library.DTO.Request.BookRequest;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.Entity.Author;
import com.app.library.Entity.Book;
import com.app.library.Entity.Genre;
import com.app.library.Entity.Publisher;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
     public BookResponse ToDto(Book book) {
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
    public void updateTheBook(Book book, BookRequest bookRequest, Genre genre, Publisher publisher, Author author) {
        book.setTitle(bookRequest.title());
        book.setpublicationDate(bookRequest.publicationDate());
        book.setIsbn(bookRequest.isbn());
        book.setLanguage(bookRequest.language());
        book.setPages(bookRequest.pages());
        book.setPrice(bookRequest.price());
        book.setOldprice(bookRequest.price());
        book.setGenre(genre);
        book.setAuthor(author);
        book.setPublisher(publisher);
    }
}
