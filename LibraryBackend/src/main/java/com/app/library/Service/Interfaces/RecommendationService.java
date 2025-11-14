package com.app.library.Service.Interfaces;

import com.app.library.DTO.Response.BookResponse;

import java.util.Set;

public interface RecommendationService {
    Set<BookResponse> generateForUser(Long userId);
}
