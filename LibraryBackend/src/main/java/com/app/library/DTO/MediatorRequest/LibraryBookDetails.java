package com.app.library.DTO.MediatorRequest;

import com.app.library.DTO.MediatorResponse.LibraryBookDetailsResponse;
import com.app.library.Entity.Book;
import com.app.library.Mediator.Interfaces.IRequest;

public record LibraryBookDetails(Long bookIsbn,String id) implements IRequest<LibraryBookDetailsResponse> {}
