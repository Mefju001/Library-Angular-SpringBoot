package com.app.library.Service;

import com.app.library.Builder.Review.ReviewBuilder;
import com.app.library.DTO.Mapper.ReviewMapper;
import com.app.library.DTO.Request.ReviewRequest;
import com.app.library.DTO.Response.ReviewAvrResponse;
import com.app.library.DTO.Response.ReviewResponse;
import com.app.library.Entity.Review;
import com.app.library.Repository.ReviewRepository;
import com.app.library.Service.Interfaces.BookService;
import com.app.library.Service.Interfaces.ReviewService;
import com.app.library.Service.Interfaces.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewBuilder reviewBuilder;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final UserService userService;
    private final BookService bookService;
    @Autowired
    public ReviewServiceImpl(ReviewBuilder reviewBuilder, ReviewRepository reviewRepository, ReviewMapper reviewMapper, UserService userService, BookService bookService) {
        this.reviewBuilder = reviewBuilder;
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.userService = userService;
        this.bookService = bookService;
    }
    @Override
    @Transactional
    public ReviewResponse add(ReviewRequest request) {
        var user = userService.getInternalUserEntity(request.userId());
        var book = bookService.getInternalBookEntity(request.bookId());
        if (reviewRepository.existsByUser_IdAndBook_Id(user.getId(), book.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already reviewed this book");
        }
        final var review = reviewBuilder.builder()
                        .content(request.content())
                                .rating(request.rating())
                                        .user(user)
                                                .book(book)
                                                        .build();
        reviewRepository.save(review);
        return reviewMapper.toDto(review);
    }

    @Override
    public List<ReviewResponse> listOfReviewForUser(long id) {
        return reviewRepository.findReviewsByUser_Id(id).stream().map(reviewMapper::toDto).toList();
    }
    @Override
    public List<ReviewAvrResponse> listAvgRatingsForBooks() {
        return reviewRepository.getAverageForBooks();
    }
    @Override
    public ReviewAvrResponse avgRatingForBook(String title) {
        return reviewRepository.getAvgForBook(title);
    }
    @Override
    public List<ReviewResponse> listReviewsForBook(String title) {
        return reviewRepository.getReviewsByBook_Title(title).stream().map(reviewMapper::toDto).toList();
    }
}
