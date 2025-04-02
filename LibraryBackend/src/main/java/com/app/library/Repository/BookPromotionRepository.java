package com.app.library.Repository;

import com.app.library.Entity.BookPromotion;
import com.app.library.Entity.Promotions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookPromotionRepository extends JpaRepository<BookPromotion,Long> {

    //BookPromotion findBookPromotionByPromotions_Id(Long id);
    List<BookPromotion> findBookPromotionByPromotions_Id(Long id);
}
