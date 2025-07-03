package com.app.library.Service;

import com.app.library.DTO.Mapper.ReviewMapper;
import com.app.library.DTO.Request.ReviewRequest;
import com.app.library.DTO.Response.ReviewAvrResponse;
import com.app.library.DTO.Response.ReviewResponse;
import com.app.library.Entity.Review;
import com.app.library.Repository.BookRepository;
import com.app.library.Repository.ReviewRepository;
import com.app.library.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;
    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, BookRepository bookRepository, UserRepository userRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.reviewMapper = reviewMapper;
    }

    @Transactional
    public ReviewResponse add(ReviewRequest request) {
        var user = userRepository.findById(request.userId());
        var book = bookRepository.findById(request.bookId());
        if (user.isEmpty() || book.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or book not found");
        var review = new Review(
                request.content(),
                request.rating(),
                LocalDate.now(),
                user.get(),
                book.get());
        reviewRepository.save(review);
        return reviewMapper.toDto(review);
    }

    @Override
    public List<ReviewResponse> listOfReviewForUser(long id) {
        List<Review> reviews = reviewRepository.findReviewsByUser_Id(id);
        return reviews.stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }
    public List<ReviewAvrResponse> listReviewsAvrForBooks() {
        return reviewRepository.getAvarageForBooks();
    }

    @Override
    public List<ReviewResponse> listReviewsForBooks(String title) {
        List<Review> reviews = reviewRepository.getReviewsByBook_Title(title);
        return reviews.stream().map(reviewMapper::toDto).collect(Collectors.toList());
    }
}
