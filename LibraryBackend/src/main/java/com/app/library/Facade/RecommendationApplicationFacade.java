package com.app.library.Facade;

import com.app.library.DTO.Response.BookResponse;
import com.app.library.Facade.Services.Recommedation.RecommendationDataLookupService;
import com.app.library.Facade.Services.Recommedation.FilterBooks;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Service
public class RecommendationApplicationFacade {
    private final RecommendationDataLookupService recommendationDataLookupService;
    private final FilterBooks filterBooks;
    public RecommendationApplicationFacade(RecommendationDataLookupService recommendationDataLookupService, FilterBooks filterBooks) {
        this.recommendationDataLookupService = recommendationDataLookupService;
        this.filterBooks = filterBooks;
    }
    public Set<BookResponse>RecommendationsForUser(Long userId)
    {
        var data = recommendationDataLookupService.generateForUser(userId);
        Set<Integer> excludedIds = Stream.concat(
                data.rentalList().stream().map(r -> r.getBook().getId()),
                data.favoriteBooks().stream().map(f -> f.getBook().getId())
        ).collect(Collectors.toSet());
        return filterBooks.selectAndSort(
                data.allBooks(),
                excludedIds,
                data.favoriteBooks(),
                data.rentalList()
        );
    }
}
