package com.app.library.DTO.Response;

import com.app.library.DTO.Request.BookRequest;
import com.app.library.Security.DTO.Request.UserRequest;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        String content,
        int rating,
        LocalDateTime createdAt,
        UserResponse user,
        BookResponse book){}
