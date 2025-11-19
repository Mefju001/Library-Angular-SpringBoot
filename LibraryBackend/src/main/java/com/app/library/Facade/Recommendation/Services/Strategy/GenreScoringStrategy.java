package com.app.library.Facade.Recommendation.Services.Strategy;

import com.app.library.Entity.Book;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Entity.Rental;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GenreScoringStrategy implements ScoringStrategies{

    @Override
    public String getStrategyName() {
        return "Genre Match";
    }

    @Override
    public double calculateScore(Book book, List<Favoritebooks> favs, List<Rental> rents) {
        boolean matchesGenre = favs.stream()
                .anyMatch(f -> f.getBook().getGenre() != null && f.getBook().getGenre().equals(book.getGenre()))
                || rents.stream()
                .anyMatch(r -> r.getBook().getGenre() != null && r.getBook().getGenre().equals(book.getGenre()));
        return matchesGenre ?0.5 : 0.0;
    }
}
