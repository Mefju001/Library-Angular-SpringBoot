package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.ReviewResponse;
import com.app.library.Entity.Review;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    ReviewResponse toDto(Review review);

}
