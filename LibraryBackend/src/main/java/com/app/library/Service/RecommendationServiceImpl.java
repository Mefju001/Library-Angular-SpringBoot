package com.app.library.Service;

import com.app.library.DTO.Response.BookResponse;
import com.app.library.Facade.Recommendation.RecommendationApplicationFacade;
import com.app.library.Service.Interfaces.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private final RecommendationApplicationFacade recommendationApplicationFacade;

    @Autowired
    public RecommendationServiceImpl(RecommendationApplicationFacade recommendationApplicationFacade) {
        this.recommendationApplicationFacade = recommendationApplicationFacade;
    }

    @Override
    public Set<BookResponse> generateForUser(Long userId) {
        return recommendationApplicationFacade.RecommendationsForUser(userId);
    }
}
