package com.app.library.Facade.Recommendation.Services;

import com.app.library.Entity.Book;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Entity.Rental;
import com.app.library.Facade.Recommendation.Services.Strategy.ScoringStrategies;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationScoreEngine {
    private final List<ScoringStrategies>scoringStrategies;
    public RecommendationScoreEngine(List<ScoringStrategies>scoringStrategies) {
        this.scoringStrategies = scoringStrategies;
    }
    public double computeScore(Book book, List<Favoritebooks> favs, List<Rental> rents) {
        double score = 0;
        for(ScoringStrategies strategy: scoringStrategies)
        {
            score += strategy.calculateScore(book,favs,rents);
        }
        return score;
    }
}
