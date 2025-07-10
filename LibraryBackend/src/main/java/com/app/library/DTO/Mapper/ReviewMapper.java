package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.ReviewResponse;
import com.app.library.Entity.Review;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "Spring", uses = {UserMapper.class, BookMapper.class})
public interface ReviewMapper {
    ReviewResponse toDto(Review review);

}
