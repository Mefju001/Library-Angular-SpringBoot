package com.app.library.Service;

import com.app.library.Entity.Book;

import java.util.Set;

public interface RecommendationService {
     Set<Book> generateForUser(Long userId);
}
