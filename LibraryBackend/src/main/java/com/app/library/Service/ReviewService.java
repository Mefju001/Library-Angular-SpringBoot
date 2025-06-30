package com.app.library.Service;

import com.app.library.DTO.Request.ReviewRequest;
import com.app.library.DTO.Response.ReviewAvrResponse;
import com.app.library.DTO.Response.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewResponse add(ReviewRequest request);

    List<ReviewResponse> listOfReviewForUser(long id);
    List<ReviewAvrResponse> listReviewsAvrForBooks();
}
