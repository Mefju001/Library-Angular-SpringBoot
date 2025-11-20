package com.app.library.Facade.Services.Strategy;

import com.app.library.Entity.Book;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Entity.Rental;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PublisherScoringStrategy implements ScoringStrategies{

    @Override
    public String getStrategyName() {
        return "Publisher Match";
    }

    @Override
    public double calculateScore(Book book, List<Favoritebooks> favs, List<Rental> rents) {
        boolean matchesPublisher = rents.stream()
                .anyMatch(r -> r.getBook().getPublisher() != null && r.getBook().getPublisher().equals(book.getPublisher()))
                || favs.stream()
                .anyMatch(f -> f.getBook().getPublisher() != null && f.getBook().getPublisher().equals(book.getPublisher()));
        return matchesPublisher ? 0.2 : 0.0;
    }
}
