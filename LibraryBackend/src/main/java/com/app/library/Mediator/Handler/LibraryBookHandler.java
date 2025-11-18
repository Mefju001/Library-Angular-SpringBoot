package com.app.library.Mediator.Handler;

import com.app.library.DTO.MediatorRequest.LibraryBookDetails;
import com.app.library.DTO.MediatorResponse.LibraryBookDetailsResponse;
import com.app.library.Mediator.Interfaces.IQueryHandler;
import com.app.library.Service.Interfaces.BookService;
import com.app.library.Service.Interfaces.LibraryService;
import com.app.library.Service.Interfaces.UserService;

public class LibraryBookHandler implements IQueryHandler<LibraryBookDetails, LibraryBookDetailsResponse> {
    private final BookService bookService;
    private final LibraryService libraryService;
    public LibraryBookHandler(BookService bookService, LibraryService libraryService) {
        this.bookService = bookService;
        this.libraryService = libraryService;
    }
    @Override
    public LibraryBookDetailsResponse handle(LibraryBookDetails request) {
        var book = bookService.findByIsbn(request.bookIsbn());
        var library = libraryService.findByAddress(request.id());
        if (book != null && library != null) {
            return new LibraryBookDetailsResponse(book, library);
        }
        return null;
    }
}
