package com.app.library.DTO.MediatorRequest;

import com.app.library.Entity.Book;
import com.app.library.Mediator.Interfaces.IRequest;

public record ExistBook(Long isbn) implements IRequest<Book> {}
