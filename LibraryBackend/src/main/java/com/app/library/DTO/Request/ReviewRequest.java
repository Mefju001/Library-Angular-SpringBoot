package com.app.library.DTO.Request;

import com.app.library.Entity.Book;
import com.app.library.Entity.User;
import com.app.library.Security.DTO.Request.UserRequest;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

public record ReviewRequest (
        String content,
        int rating,
        LocalDateTime createdAt,
        Long userId,
        Integer bookId){}
