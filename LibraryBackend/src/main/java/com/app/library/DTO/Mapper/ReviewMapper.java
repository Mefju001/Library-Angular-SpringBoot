package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.ReviewResponse;
import com.app.library.Entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public ReviewMapper(UserMapper userMapper, BookMapper bookMapper) {
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    public ReviewResponse toDto(Review review)
    {
        return new ReviewResponse(
                review.getId(),
                review.getContent(),
                review.getRating(),
                review.getCreatedAt(),
                userMapper.toDto(review.getUser()),
                bookMapper.ToDto(review.getBook())
        );
    }

}
