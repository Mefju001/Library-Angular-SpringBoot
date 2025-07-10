package com.app.library.Service;

import com.app.library.DTO.Mapper.BookMapper;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.Entity.Book;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Entity.Rental;
import com.app.library.Repository.BookRepository;
import com.app.library.Repository.FavoritebooksRepository;
import com.app.library.Repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private final FavoritebooksRepository favoritebooksRepository;
    private final RentalRepository rentalRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Autowired
    public RecommendationServiceImpl(FavoritebooksRepository favoritebooksRepository, RentalRepository rentalRepository, BookRepository bookRepository, BookMapper bookMapper) {
        this.favoritebooksRepository = favoritebooksRepository;
        this.rentalRepository = rentalRepository;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public Set<BookResponse> generateForUser(Long userId) {
        List<Favoritebooks> favoriteBooks = favoritebooksRepository.findFavoritebooksByUser_Id(userId);
        List<Rental> rentalList = rentalRepository.findRentalsByUser_Id(userId);
        List<Book> allBooks = bookRepository.findAll();
        return filterBooks(rentalList, favoriteBooks, allBooks);
    }

    private Set<BookResponse> filterBooks(List<Rental> rents, List<Favoritebooks> favs, List<Book> allBooks) {
        Set<Integer> rentedBookIds = rents.stream()
                .map(r -> r.getBook().getId())
                .collect(Collectors.toSet());

        Set<Integer> favoriteBookIds = favs.stream()
                .map(f -> f.getBook().getId())
                .collect(Collectors.toSet());

        return allBooks.stream()
                .filter(book -> !rentedBookIds.contains(book.getId()) && !favoriteBookIds.contains(book.getId()))
                .map(book -> new AbstractMap.SimpleEntry<>(book, computeScore(book, favs, rents)))
                .filter(entry -> entry.getValue() > 0.2)
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(10)
                .map(Map.Entry::getKey)
                .map(this::mapBookToBookResponse)
                .collect(Collectors.toSet());
    }
    private BookResponse mapBookToBookResponse(Book book) {
        // Zaimplementuj tutaj swoją logikę mapowania
        // Przykład:
        return bookMapper.toDto(book);
    }
    private double computeScore(Book book, List<Favoritebooks> favs, List<Rental> rents) {
        double score = 0.0;

        boolean matchesGenre = favs.stream()
                .anyMatch(f -> f.getBook().getGenre() != null && f.getBook().getGenre().equals(book.getGenre()))
                || rents.stream()
                .anyMatch(r -> r.getBook().getGenre() != null && r.getBook().getGenre().equals(book.getGenre()));
        if (matchesGenre) {
            score += 0.5;
        }

        boolean matchesAuthor = rents.stream()
                .anyMatch(r -> r.getBook().getAuthor() != null && r.getBook().getAuthor().equals(book.getAuthor()))
                || favs.stream()
                .anyMatch(f -> f.getBook().getAuthor() != null && f.getBook().getAuthor().equals(book.getAuthor()));
        if (matchesAuthor) {
            score += 0.3;
        }
        boolean matchesPublisher = rents.stream()
                .anyMatch(r -> r.getBook().getPublisher() != null && r.getBook().getPublisher().equals(book.getPublisher()))
                || favs.stream()
                .anyMatch(f -> f.getBook().getPublisher() != null && f.getBook().getPublisher().equals(book.getPublisher()));
        if (matchesPublisher) {
            score += 0.2;
        }
        // Popularność (np. ile razy wypożyczona)

        return score;
    }

}
