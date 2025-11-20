package com.app.library.Builder.Review;

import com.app.library.Entity.Book;
import com.app.library.Entity.Review;
import com.app.library.Entity.User;
import org.springframework.stereotype.Component;

@Component
public class ReviewBuilder implements IReviewBuilder{
    private Review review;
    @Override
    public IReviewBuilder builder() {
        this.review = new Review();
        return this;
    }

    @Override
    public IReviewBuilder content(String content) {
        this.review.setContent(content);
        return this;
    }

    @Override
    public IReviewBuilder rating(Integer rating) {
        this.review.setRating(rating);
        return this;
    }

    @Override
    public IReviewBuilder user(User user) {
        this.review.setUser(user);
        return this;
    }

    @Override
    public IReviewBuilder book(Book book) {
        this.review.setBook(book);
        return this;
    }

    @Override
    public Review build() {
        return review;
    }
}
