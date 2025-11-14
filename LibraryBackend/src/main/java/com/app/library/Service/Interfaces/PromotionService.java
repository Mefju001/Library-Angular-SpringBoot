package com.app.library.Service.Interfaces;

public interface PromotionService {
    void setpromotion(Integer bookId, long promotionId);

    void deactivatePromotion(Long promotionId);

    Boolean deleteBookPromotion(Long bookPromotionId);
}
