package com.app.library.Builder;

import com.app.library.Entity.Author;
import com.app.library.Entity.Book;
import com.app.library.Entity.Genre;
import com.app.library.Entity.Publisher;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class BookBuilder implements IBookBuilder {
    private Book book;
    @Override
    public IBookBuilder CreateNewBook(String title,Long isbn,Float price) {
        book = new Book(title,isbn,price);
        return this;
    }

    @Override
    public IBookBuilder WithDetails(LocalDate publicationDate,String language,int pages) {
        if(publicationDate==null)throw new NullPointerException("Publication Date cannot be null");
        if(language==null)throw new NullPointerException("Language cannot be null");
        if(pages<0)throw new IllegalArgumentException("Pages cannot be negative");
        book.setLanguage(language);
        book.setPages(pages);
        book.setpublicationDate(publicationDate);
        book.setOldprice(book.getPrice());
        return this;
    }

    @Override
    public IBookBuilder WithAuthor(Author author) {
        if(author==null)throw new NullPointerException("Author can't be null");
        book.setAuthor(author);
        return this;
    }

    @Override
    public IBookBuilder WithPublisher(Publisher publisher) {
        if(publisher==null)throw new NullPointerException("Publisher can't be null");
        book.setPublisher(publisher);
        return this;
    }

    @Override
    public IBookBuilder WithGenre(Genre genre) {
        if(genre==null)throw new NullPointerException("Genre can't be null");
        book.setGenre(genre);
        return this;
    }

    @Override
    public Book build() {
        if(book.getGenre() == null||book.getAuthor()==null||book.getPublisher()==null){
            throw new NullPointerException("Book Genre or Author or Publisher");
        }
        return book;
    }
}
