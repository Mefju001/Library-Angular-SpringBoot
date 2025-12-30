package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.LibraryBookResponse;
import com.app.library.Entity.Book;
import com.app.library.Entity.Library;
import com.app.library.Entity.LibraryBook;
import org.springframework.stereotype.Component;

@Component
public class LibraryBookMapper
{
    private final BookMapper bookMapper;
    private final LibraryMapper libraryMapper;

    public LibraryBookMapper(BookMapper bookMapper, LibraryMapper libraryMapper) {
        this.bookMapper = bookMapper;
        this.libraryMapper = libraryMapper;
    }
    public LibraryBookResponse toDto(LibraryBook libraryBook)
    {
        return new LibraryBookResponse(libraryBook.getId(), bookMapper.ToDto(libraryBook.getBook()),libraryMapper.toDto(libraryBook.getLibrary()),libraryBook.getStock());
    }
    public void UpdateLibraryBook(LibraryBook libraryBook, Library library, Book book,int stock)
    {
        libraryBook.setLibrary(library);
        libraryBook.setBook(book);
        libraryBook.setStock(stock);
    }
}
