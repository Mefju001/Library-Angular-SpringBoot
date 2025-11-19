package com.app.library.Facade.Recommendation;

import com.app.library.DTO.Response.BookResponse;
import com.app.library.Facade.Recommendation.Services.DataLookupService;
import com.app.library.Facade.Recommendation.Services.FilterBooks;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Service
public class RecommendationApplicationFacade {
    private final DataLookupService dataLookupService;
    private final FilterBooks filterBooks;
    public RecommendationApplicationFacade(DataLookupService dataLookupService, FilterBooks filterBooks) {
        this.dataLookupService = dataLookupService;
        this.filterBooks = filterBooks;
    }
    public Set<BookResponse>RecommendationsForUser(Long userId)
    {
        var data = dataLookupService.generateForUser(userId);
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
