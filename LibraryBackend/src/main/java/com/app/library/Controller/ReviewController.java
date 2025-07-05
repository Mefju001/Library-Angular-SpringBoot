package com.app.library.Controller;

import com.app.library.DTO.Request.ReviewRequest;
import com.app.library.DTO.Response.ReviewAvrResponse;
import com.app.library.DTO.Response.ReviewResponse;
import com.app.library.Service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    @Operation(summary = "", description = "")
    public ResponseEntity<ReviewResponse> addfavoritebooks(@RequestBody ReviewRequest request) {
        ReviewResponse response = reviewService.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/user/{userid}")
    @Operation(summary = "", description = "")
    public ResponseEntity<List<ReviewResponse>> getReviewListByUser(@PathVariable long userid) {
        List<ReviewResponse> response = reviewService.listOfReviewForUser(userid);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/Avg")
    @Operation(summary = "", description = "")
    public ResponseEntity<List<ReviewAvrResponse>> getAvarageReviewsForBooks() {
        List<ReviewAvrResponse> response = reviewService.listReviewsAvrForBooks();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/Avg/title/{BookTitle}")
    @Operation(summary = "", description = "")
    public ResponseEntity<ReviewAvrResponse> getAvgForTitle(@PathVariable String BookTitle) {
        ReviewAvrResponse response = reviewService.AvgForBook(BookTitle);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/title/{title}")
    @Operation(summary = "", description = "")
    public ResponseEntity<List<ReviewResponse>> getReviewsForBooks(@PathVariable String title) {
        List<ReviewResponse> response = reviewService.listReviewsForBooks(title);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
