package com.app.library.Facade.Recommendation.Services;

import com.app.library.DTO.Mapper.DataReturn;
import com.app.library.Entity.Book;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Entity.Rental;
import com.app.library.Repository.BookRepository;
import com.app.library.Repository.FavoritebooksRepository;
import com.app.library.Repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataLookupService {
    private final FavoritebooksRepository favoritebooksRepository;
    private final RentalRepository rentalRepository;
    private final BookRepository bookRepository;

    @Autowired
    public DataLookupService(FavoritebooksRepository favoritebooksRepository, RentalRepository rentalRepository, BookRepository bookRepository) {
        this.favoritebooksRepository = favoritebooksRepository;
        this.rentalRepository = rentalRepository;
        this.bookRepository = bookRepository;
    }
    public DataReturn generateForUser(Long userId) {
        List<Favoritebooks> favoriteBooks = favoritebooksRepository.findFavoritebooksByUser_Id(userId);
        List<Rental> rentalList = rentalRepository.findRentalsByUser_Id(userId);
        List<Book> allBooks = bookRepository.findAll();
        return new DataReturn(favoriteBooks, rentalList, allBooks);
    }

}
