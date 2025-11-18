package com.app.library.Service;

import com.app.library.Entity.Promotions;
import com.app.library.Facade.Promotion.PromotionApplicationFacade;
import com.app.library.Repository.PromotionRepository;
import com.app.library.Service.Interfaces.PromotionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final PromotionApplicationFacade  promotionApplicationFacade;

    public PromotionServiceImpl(PromotionRepository promotionRepository, PromotionApplicationFacade promotionApplicationFacade) {
        this.promotionRepository = promotionRepository;
        this.promotionApplicationFacade = promotionApplicationFacade;
    }

    @Override
    @Transactional
    public void setPromotion(Integer bookId, long promotionId) {
        promotionApplicationFacade.applyPromotion(bookId,promotionId);
    }

    @Override
    @Transactional
    public void deactivatePromotion(Long promotionId) {
        Optional<Promotions> promotionsOptional = promotionRepository.findById(promotionId);
        if (promotionsOptional.isPresent()) {
            Promotions promotion = promotionsOptional.get();
            promotion.setActive(false);
            promotionApplicationFacade.deactiveBookPromotion(promotionId);
        }
    }

    @Override
    @Transactional
    public Boolean deleteBookPromotion(Long bookPromotionId) {
        return promotionApplicationFacade.removeBookPromotion(bookPromotionId);
    }

}
