package com.app.library.Facade.Services.Strategy;

import com.app.library.Entity.Book;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Entity.Rental;

import java.util.List;

public interface ScoringStrategies {
    String getStrategyName();
    double calculateScore(Book book, List<Favoritebooks> favs, List<Rental> rents);
}
