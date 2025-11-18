package com.app.library.DTO.Response;

import lombok.Builder;

@Builder
public record FavoriteBooksResponse(BookResponse book, UserResponse user) {
}
