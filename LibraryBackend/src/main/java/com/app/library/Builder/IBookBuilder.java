package com.app.library.Builder;

import com.app.library.Entity.Author;
import com.app.library.Entity.Book;
import com.app.library.Entity.Genre;
import com.app.library.Entity.Publisher;

import java.time.LocalDate;

public interface IBookBuilder {
    IBookBuilder CreateNewBook(String title,Long isbn,Float price);
    IBookBuilder WithDetails(LocalDate publicationDate, String language, int pages);
    IBookBuilder WithAuthor(Author author);
    IBookBuilder WithPublisher(Publisher publisher);
    IBookBuilder WithGenre(Genre genre);
    Book build();
}
