package com.app.library.Builder.Review;

import com.app.library.Entity.Book;
import com.app.library.Entity.Review;
import com.app.library.Entity.User;

public interface IReviewBuilder {
    IReviewBuilder builder();
    IReviewBuilder content(String content);
    IReviewBuilder rating(Integer rating);
    IReviewBuilder user(User user);
    IReviewBuilder book(Book book);
    Review build();
}
