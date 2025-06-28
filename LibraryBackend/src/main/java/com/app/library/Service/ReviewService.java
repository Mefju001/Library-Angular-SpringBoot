package com.app.library.Service;

import com.app.library.DTO.Request.ReviewRequest;
import com.app.library.DTO.Response.ReviewResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ReviewService {
    ReviewResponse add(ReviewRequest request);
    List<ReviewResponse>listOfReviewForUser(long id);
}
