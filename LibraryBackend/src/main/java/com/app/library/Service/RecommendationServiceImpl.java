package com.app.library.Service;

import com.app.library.DTO.Response.BookResponse;

import java.util.List;

public class RecommendationServiceImpl implements RecommendationService {
    @Override
    public List<BookResponse> generateForUser(Long userId) {
        return null;
    }
    /*
            Set<Long> favoriteIds = favoritebooksRepository.findBookIdsByUser(userId);
        Set<Long> rentedIds = rentalRepository.findBookIdsByUser(userId);

        List<Book> allBooks = bookRepository.findAll();

        // Scoring i filtrowanie książek
        return allBooks.stream()
                .filter(book -> !favoriteIds.contains(book.getId()) && !rentedIds.contains(book.getId()))
                .map(book -> new AbstractMap.SimpleEntry<>(book, computeScore(book, favoriteIds, rentedIds)))
                .filter(entry -> entry.getValue() > 0.2)
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(10)
                .map(Map.Entry::getKey)
                .toList();
    }

    private double computeScore(Book book, Set<Long> favs, Set<Long> rents) {
        double score = 0.0;

        // Rozbudować!
        if (book.getCategory() != null && favs.stream().anyMatch(id -> id.equals(book.getId()))) {
            score += 0.5;
        }

        if (book.getCategory() != null && rents.stream().anyMatch(id -> id.equals(book.getId()))) {
            score += 0.3;
        }

        //Przyszła rozbudowa odnośnie punktacji na podstawie popularności, ocen itp.

        return score;
    }
     */
}
