package com.app.library.Facade.Recommendation.Services;

import com.app.library.DTO.Mapper.BookMapper;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.Entity.Book;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Entity.Rental;
import com.app.library.Service.Interfaces.RecommendationService;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class FilterBooks {
    private final BookMapper bookMapper;
    private final RecommendationScoreEngine scoreEngine;
    public FilterBooks(BookMapper bookMapper, RecommendationScoreEngine scoreEngine) {

        this.bookMapper = bookMapper;
        this.scoreEngine = scoreEngine;
    }
    public Set<BookResponse> selectAndSort(List<Book> allBooks, Set<Integer> excludedIds, List<Favoritebooks> favs, List<Rental> rents)
    {
        return allBooks.stream()
                .filter(book -> !excludedIds.contains(book.getId())) // Użycie gotowej listy wykluczeń
                .map(book -> new AbstractMap.SimpleEntry<>(book, scoreEngine.computeScore(book, favs, rents))) // <--- POPRAWNIE DELEGUJE OBLICZENIA
                .filter(entry -> entry.getValue() > 0.2)
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(10)
                .map(Map.Entry::getKey)
                .map(bookMapper::ToDto)
                .collect(Collectors.toSet());
    }
}
