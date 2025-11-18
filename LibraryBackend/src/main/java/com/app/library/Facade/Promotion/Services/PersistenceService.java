package com.app.library.Facade.Promotion.Services;

import com.app.library.Entity.Book;
import com.app.library.Entity.BookPromotion;
import com.app.library.Entity.Promotions;
import com.app.library.Repository.BookPromotionRepository;
import com.app.library.Repository.BookRepository;
import com.app.library.Service.PromotionServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PersistenceService {
    private final BookPromotionRepository bookPromotionRepository;

    private final BookRepository bookRepository;

    public PersistenceService(BookPromotionRepository bookPromotionRepository, BookRepository bookRepository) {
        this.bookPromotionRepository = bookPromotionRepository;
        this.bookRepository = bookRepository;
    }

    public void savePromotionDetails(Book book, Promotions promotion, BigDecimal finalPrice) {
        book.setPrice(finalPrice.floatValue());
        bookRepository.save(book);
        BookPromotion bookPromotion = new BookPromotion(book, promotion);
        bookPromotionRepository.save(bookPromotion);
    }

    public Boolean deleteBookPromotionWithCleanup(Long bookPromotionId)
    {
        Optional<BookPromotion> bookPromotionOptional = bookPromotionRepository.findById(bookPromotionId);

        if (bookPromotionOptional.isPresent()) {
            BookPromotion bookPromotion = bookPromotionOptional.get();
            Book book = bookPromotion.getBook();

            if (book.getOldprice() != -1.0f) {
                book.setPrice(book.getOldprice());
            }
            bookRepository.save(book);
            bookPromotionRepository.deleteById(bookPromotionId);
            return true;
        }
        return false;
    }
}
