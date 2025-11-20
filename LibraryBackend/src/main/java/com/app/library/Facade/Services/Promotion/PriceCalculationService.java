package com.app.library.Facade.Services.Promotion;

import com.app.library.Entity.Book;
import com.app.library.Entity.DiscountType;
import com.app.library.Entity.Promotions;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PriceCalculationService {
    public BigDecimal calculateFinalPrice(Book book, Promotions promotion) {
        BigDecimal finalPrice = BigDecimal.valueOf(book.getPrice());
        if(promotion.getDiscountType()==DiscountType.percentage)
        {
            finalPrice = finalPrice.subtract(finalPrice.multiply(promotion.getDiscountValue().divide(BigDecimal.valueOf(100))));
            finalPrice = finalPrice.setScale(2, RoundingMode.HALF_UP);
        } else if(promotion.getDiscountType()==DiscountType.fixed)
        {
            finalPrice = finalPrice.subtract(promotion.getDiscountValue());
            finalPrice = finalPrice.setScale(2, RoundingMode.HALF_UP);
        }
        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
            finalPrice = BigDecimal.valueOf(1);
            finalPrice = finalPrice.setScale(2, RoundingMode.HALF_UP);
        }
        return finalPrice;
    }
    
}
