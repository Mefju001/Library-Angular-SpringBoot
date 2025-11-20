package com.app.library.Facade;

import com.app.library.Entity.Book;
import com.app.library.Entity.BookPromotion;
import com.app.library.Entity.Promotions;
import com.app.library.Facade.Services.Promotion.DataLookupService;
import com.app.library.Facade.Services.Promotion.PersistenceService;
import com.app.library.Facade.Services.Promotion.PriceCalculationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PromotionApplicationFacade {
    private final DataLookupService dataLookupService;
    private final PriceCalculationService priceCalculationService;
    private final PersistenceService persistenceService;
    public PromotionApplicationFacade(DataLookupService dataLookupService, PriceCalculationService priceCalculationService, PersistenceService persistenceService) {
        this.dataLookupService = dataLookupService;
        this.priceCalculationService = priceCalculationService;
        this.persistenceService = persistenceService;
    }
    @Transactional
    public void applyPromotion(Integer bookId, Long promotionId) {
        Book book = dataLookupService.findBook(bookId);
        Promotions promotion = dataLookupService.findPromotions(promotionId);

        BigDecimal finalPrice = priceCalculationService.calculateFinalPrice(book, promotion);

        persistenceService.savePromotionDetails(book,promotion,finalPrice);
    }
    @Transactional
    public Boolean removeBookPromotion(Long bookPromotionId)
    {
        return persistenceService.deleteBookPromotionWithCleanup(bookPromotionId);
    }
    @Transactional
    public void deactiveBookPromotion(Long bookPromotionId)
    {

        List<BookPromotion> bookPromotions = dataLookupService.findBookPromotion(bookPromotionId);
        for (BookPromotion bookPromotion : bookPromotions) {
            removeBookPromotion(bookPromotion.getId());
        }
    }
}
