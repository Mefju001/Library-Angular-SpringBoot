package com.app.library.Repository;

import com.app.library.Entity.Promotions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PromotionRepository extends JpaRepository<Promotions,Long> {
    @Query("SELECT p FROM Promotions p JOIN BookPromotion pp ON p.id = pp.promotions.id " +
            "WHERE pp.book.id = :productId AND p.isActive = TRUE ")
    Optional<Promotions> findActivePromotionByProductId(@Param("productId") Integer productId);

    Optional<Promotions> findPromotionsByIdAndIsActive(Long id,Boolean active);

}
