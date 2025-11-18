package com.app.library.DTO.MediatorResponse;

import com.app.library.Entity.Book;
import com.app.library.Entity.Library;
import com.app.library.Entity.User;

public record LibraryBookDetailsResponse(Book book, Library library) {}
