package com.app.library.Facade.Services.Promotion;

import com.app.library.Entity.Book;
import com.app.library.Entity.BookPromotion;
import com.app.library.Entity.Promotions;
import com.app.library.Repository.BookPromotionRepository;
import com.app.library.Repository.BookRepository;
import com.app.library.Repository.PromotionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataLookupService {
    private final BookRepository bookRepository;
    private final PromotionRepository promotionRepository;
    private final BookPromotionRepository bookPromotionRepository;

    public DataLookupService(BookRepository bookRepository, PromotionRepository promotionRepository, BookPromotionRepository bookPromotionRepository) {
        this.bookRepository = bookRepository;
        this.promotionRepository = promotionRepository;
        this.bookPromotionRepository = bookPromotionRepository;
    }
    public Book findBook(Integer id) {
        return bookRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
    public Promotions findPromotions(Long id) {
        return promotionRepository.findPromotionsByIdAndIsActive(id,true).orElseThrow(EntityNotFoundException::new);
    }
    public List<BookPromotion> findBookPromotion(Long id) {
        return bookPromotionRepository.findBookPromotionByPromotions_Id(id);
    }
}
