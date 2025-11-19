package com.app.library.Facade.Recommendation.Services.Strategy;

import com.app.library.Entity.Book;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Entity.Rental;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AuthorScoringStrategy implements ScoringStrategies{

    @Override
    public String getStrategyName() {
        return "Author Match";
    }

    @Override
    public double calculateScore(Book book, List<Favoritebooks> favs, List<Rental> rents) {
        boolean matchesAuthor = rents.stream()
                .anyMatch(r -> r.getBook().getAuthor() != null && r.getBook().getAuthor().equals(book.getAuthor()))
                || favs.stream()
                .anyMatch(f -> f.getBook().getAuthor() != null && f.getBook().getAuthor().equals(book.getAuthor()));
        return matchesAuthor ? 0.3 : 0.0;
    }
}
