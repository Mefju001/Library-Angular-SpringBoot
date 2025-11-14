package com.app.library.Service.Interfaces;

import com.app.library.DTO.Request.ReviewRequest;
import com.app.library.DTO.Response.ReviewAvrResponse;
import com.app.library.DTO.Response.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewResponse add(ReviewRequest request);
    ReviewAvrResponse AvgForBook(String Title);
    List<ReviewResponse> listOfReviewForUser(long id);
    List<ReviewAvrResponse> listReviewsAvrForBooks();
    List<ReviewResponse> listReviewsForBooks(String title);
}
