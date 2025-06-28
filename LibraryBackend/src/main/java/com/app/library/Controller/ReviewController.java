package com.app.library.Controller;

import com.app.library.DTO.Request.ReviewRequest;
import com.app.library.DTO.Response.ReviewResponse;
import com.app.library.Service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    @Operation(summary = "", description = "")
    public ResponseEntity<ReviewResponse> addfavoritebooks(@RequestBody ReviewRequest request) {
        ReviewResponse response = reviewService.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/reviews/{userid}")
    @Operation(summary = "", description = "")
    public ResponseEntity<List<ReviewResponse>> addfavoritebooks(@PathVariable long userid) {
        List<ReviewResponse> response = reviewService.listOfReviewForUser(userid);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
