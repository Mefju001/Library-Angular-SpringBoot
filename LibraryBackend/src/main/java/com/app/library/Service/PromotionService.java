package com.app.library.Service;

import com.app.library.Entity.Book;
import com.app.library.Entity.BookPromotion;
import com.app.library.Entity.DiscountType;
import com.app.library.Entity.Promotions;
import com.app.library.Repository.BookPromotionRepository;
import com.app.library.Repository.BookRepository;
import com.app.library.Repository.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;

    private final BookRepository bookRepository;
    private final BookPromotionRepository bookPromotionRepository;

    public PromotionService(PromotionRepository promotionRepository, BookRepository bookRepository, BookPromotionRepository bookPromotionRepository) {
        this.promotionRepository = promotionRepository;
        this.bookRepository = bookRepository;
        this.bookPromotionRepository = bookPromotionRepository;
    }

   /* @Transactional
    public void isActive(Long promotionId) {
        bookPromotionRepository.findById(promotionId).ifPresent(bookPromotion -> {
            Promotions promotion = bookPromotion.getPromotions();
            if (promotion != null && Boolean.FALSE.equals(promotion.getActive())) {
                BookPromotion bookPromotionToDelete = bookPromotionRepository.findBookPromotionByPromotions_Id(promotionId);
                if (bookPromotionToDelete != null) {
                    deletebookpromotion(bookPromotionToDelete.getId());
                }
            }
        });
    }*/

    @Transactional
    public void setpromotion(Integer bookId, long promotionId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Produkt nie znaleziony"));
        BigDecimal finalPrice = BigDecimal.valueOf(book.getPrice());
        Optional<Promotions> promotionOpt = promotionRepository.findPromotionsByIdAndIsActive(promotionId,true);
        if (promotionOpt.isPresent()) {
            Promotions promotion = promotionOpt.get();
            if (promotion.getDiscountType() == DiscountType.percentage) {
                finalPrice = finalPrice.subtract(finalPrice.multiply(promotion.getDiscountValue().divide(BigDecimal.valueOf(100))));
                finalPrice = finalPrice.setScale(2, RoundingMode.HALF_UP);
                book.setPrice(finalPrice.floatValue());
                bookRepository.save(book);
                BookPromotion bookPromotion = new BookPromotion(book,promotion);
                bookPromotionRepository.save(bookPromotion);
            } else if (promotion.getDiscountType() == DiscountType.fixed) {
                finalPrice = finalPrice.subtract(promotion.getDiscountValue());
                finalPrice = finalPrice.setScale(2, RoundingMode.HALF_UP);
                book.setPrice(finalPrice.floatValue());
                bookRepository.save(book);
                BookPromotion bookPromotion = new BookPromotion(book,promotion);
                bookPromotionRepository.save(bookPromotion);
            }

            if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
                finalPrice = BigDecimal.valueOf(1);
                finalPrice = finalPrice.setScale(2, RoundingMode.HALF_UP);
                book.setPrice(finalPrice.floatValue());
                bookRepository.save(book);
                BookPromotion bookPromotion = new BookPromotion(book,promotion);
                bookPromotionRepository.save(bookPromotion);
            }
        }
    }
    @Transactional
    public void deactivatePromotion(Long promotionId) {
        Optional<Promotions> promotionsOptional = promotionRepository.findById(promotionId);

        if (promotionsOptional.isPresent()) {
            Promotions promotion = promotionsOptional.get();
            promotion.setActive(false); // Dezaktywacja promocji

            List<BookPromotion> bookPromotions = bookPromotionRepository.findBookPromotionByPromotions_Id(promotion.getId());
            for (BookPromotion bookPromotion : bookPromotions) {
                deleteBookPromotion(bookPromotion.getId());
            }
        }
    }

    @Transactional
    public Boolean deleteBookPromotion(Long bookPromotionId) {
        Optional<BookPromotion> bookPromotionOptional = bookPromotionRepository.findById(bookPromotionId);

        if (bookPromotionOptional.isPresent()) {
            BookPromotion bookPromotion = bookPromotionOptional.get();
            Book book = bookPromotion.getBook();

            if (book.getOldprice() != -1.0f) { // Sprawdzenie czy stara cena istnieje
                book.setPrice(book.getOldprice());
            }
            bookRepository.save(book);
            bookPromotionRepository.deleteById(bookPromotionId);
            return true;
        }
        return false;
    }

}
