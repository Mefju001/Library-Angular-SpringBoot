package com.app.library.Facade.Recommendation.Services;

import com.app.library.Entity.Book;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Entity.Rental;

import java.util.List;

public class ComputeScore
{
    public double computeScore(Book book, List<Favoritebooks> favs, List<Rental> rents) {
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
