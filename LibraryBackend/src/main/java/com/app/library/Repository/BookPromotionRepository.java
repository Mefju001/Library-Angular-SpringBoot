package com.app.library.Repository;

import com.app.library.Entity.BookPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookPromotionRepository extends JpaRepository<BookPromotion, Long> {

    //BookPromotion findBookPromotionByPromotions_Id(Long id);
    List<BookPromotion> findBookPromotionByPromotions_Id(Long id);
}
