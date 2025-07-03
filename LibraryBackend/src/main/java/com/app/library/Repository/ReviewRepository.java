package com.app.library.Repository;

import com.app.library.DTO.Response.ReviewAvrResponse;
import com.app.library.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findReviewsByUser_Id(Long id);
    @Query("SELECT NEW com.app.library.DTO.Response.ReviewAvrResponse(r.book.id, r.book.title, AVG(r.rating)) " +
            "FROM Review r GROUP BY r.book.id, r.book.title")
    List<ReviewAvrResponse>getAvarageForBooks();

    List<Review> getReviewsByBook_Title(String bookTitle);
}