package com.app.library.Service;

import com.app.library.DTO.Response.BookResponse;

import java.util.List;

public interface RecommendationService {
    public List<BookResponse> generateForUser(Long userId);
}
