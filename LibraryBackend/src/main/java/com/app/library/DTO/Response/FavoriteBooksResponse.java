package com.app.library.DTO.Response;

import lombok.Builder;

@Builder
public record FavoriteBooksResponse(Integer id, BookResponse book, UserResponse user) {
}
