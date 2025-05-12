package com.app.library.Service;

import com.app.library.Entity.Book;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Entity.Rental;
import com.app.library.Repository.BookRepository;
import com.app.library.Repository.FavoritebooksRepository;
import com.app.library.Repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private final FavoritebooksRepository favoritebooksRepository;
    private final RentalRepository rentalRepository;
    private final BookRepository bookRepository;
    @Autowired
    public RecommendationServiceImpl(FavoritebooksRepository favoritebooksRepository, RentalRepository rentalRepository, BookRepository bookRepository) {
        this.favoritebooksRepository = favoritebooksRepository;
        this.rentalRepository = rentalRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Set<Book> generateForUser(Long userId) {
        Set<Long> favoriteIds = favoritebooksRepository.findBook_IdByUser_Id(userId);
        Set<Long> rentedIds = rentalRepository.findBook_IdByUser_Id(userId);

        List<Favoritebooks>favoriteBooks = favoritebooksRepository.findFavoritebooksByUser_Id(userId);
        List<Rental>rentalList = rentalRepository.findRentalsByUser_Id(userId);
        List<Book> allBooks = bookRepository.findAll();
        // Scoring i filtrowanie książek
        return filterBooks(rentalList,favoriteBooks,allBooks);
    }
    private Set<Book>filterBooks(List<Rental> rents, List<Favoritebooks> favs,List<Book> allBooks)
    {
        Set<Integer> rentedBookIds = rents.stream()
                .map(r -> r.getBook().getId())
                .collect(Collectors.toSet());

        Set<Integer> favoriteBookIds = favs.stream()
                .map(f -> f.getBook().getId())
                .collect(Collectors.toSet());

        return allBooks.stream()
                .filter(book -> !rentedBookIds.contains(book.getId()) && !favoriteBookIds.contains(book.getId()))
                .collect(Collectors.toSet());
    }

}
