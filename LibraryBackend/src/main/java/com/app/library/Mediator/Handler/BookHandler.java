package com.app.library.Mediator.Handler;

import com.app.library.DTO.MediatorRequest.ExistBook;
import com.app.library.Entity.Book;
import com.app.library.Mediator.Interfaces.IQueryHandler;
import com.app.library.Mediator.Interfaces.IRequest;
import com.app.library.Service.Interfaces.BookService;

public class BookHandler implements IQueryHandler<ExistBook,Book> {
    private final BookService bookService;
    public BookHandler(BookService bookService) {
        this.bookService = bookService;
    }
    @Override
    public Book handle(ExistBook existBook) {
        return bookService.findByIsbn(existBook.isbn());
    }
}
