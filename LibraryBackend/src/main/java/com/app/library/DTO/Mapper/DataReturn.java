package com.app.library.DTO.Mapper;

import com.app.library.Entity.Book;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Entity.Rental;

import java.util.List;

public record DataReturn(List<Favoritebooks> favoriteBooks,List<Rental> rentalList,List<Book> allBooks) {
}
