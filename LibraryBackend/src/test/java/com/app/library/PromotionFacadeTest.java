package com.app.library;

import com.app.library.DTO.Mapper.LibraryMapper;
import com.app.library.DTO.MediatorRequest.AuditRequest;
import com.app.library.Entity.*;
import com.app.library.Facade.PromotionApplicationFacade;
import com.app.library.Facade.Services.Promotion.DataLookupService;
import com.app.library.Facade.Services.Promotion.PersistenceService;
import com.app.library.Facade.Services.Promotion.PriceCalculationService;
import com.app.library.Mediator.Mediator;
import com.app.library.Repository.BookPromotionRepository;
import com.app.library.Repository.BookRepository;
import com.app.library.Repository.LibraryRepository;
import com.app.library.Repository.PromotionRepository;
import com.app.library.Service.LibraryServiceImpl;
import com.app.library.Service.PromotionServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PromotionFacadeTest {
    @Spy
    private PriceCalculationService priceCalculationService;
    private PromotionApplicationFacade  promotionApplicationFacade;
    @Mock
    private DataLookupService dataLookupService;
    private PersistenceService persistenceService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookPromotionRepository bookPromotionRepository;
    @BeforeEach
    void setUp() {
        persistenceService = new PersistenceService(bookPromotionRepository, bookRepository);
        persistenceService = spy(persistenceService);
        promotionApplicationFacade = new PromotionApplicationFacade(
                dataLookupService,
                priceCalculationService,
                persistenceService
        );
        promotionApplicationFacade = spy(promotionApplicationFacade);
    }
    private Book getBook(){
        return new Book(
                new Author("J.R.R","Tolkien"),
                "Władca Pierścieni",
                new Publisher("Zysk i S-ka"),
                LocalDate.now(),
                100f,
                500,
                10.0f,
                "Polski",
                1234567890L,
                1,
                new Genre("Fantasy")
        );
    }
    private Promotions getPromotion(){
        return new Promotions();
    }
    @Test
    void applyPromotion()
    {
        var book = getBook();
        var promotion = getPromotion();
        promotion.setDiscountType(DiscountType.percentage);
        promotion.setDiscountValue(new BigDecimal("20.00"));
        final var id = 1;
        when(dataLookupService.findBook(anyInt())).thenReturn(book);
        when(dataLookupService.findPromotions(anyLong())).thenReturn(promotion);
        promotionApplicationFacade.applyPromotion(id,(long)id);
        verify(dataLookupService).findBook(id);
        verify(dataLookupService).findPromotions((long)id);
        verify(priceCalculationService).calculateFinalPrice(any(Book.class),any(Promotions.class));
        verify(persistenceService).savePromotionDetails(any(Book.class),any(Promotions.class),any(BigDecimal.class));
        verify(bookRepository).save(any(Book.class));
        verify(bookPromotionRepository).save(any(BookPromotion.class));
        assertEquals(80f,book.getPrice());
    }
    @Test
    void deactivatePromotion(){
        final var id = 1L;
        BookPromotion bp1 = new BookPromotion();
        bp1.setId(101L);
        BookPromotion bp2 = new BookPromotion();
        bp2.setId(102L);

        List<BookPromotion> booksPromotion = List.of(bp1, bp2);
        when(dataLookupService.findBookPromotion(anyLong())).thenReturn(booksPromotion);
        promotionApplicationFacade.deactiveBookPromotion(id);
        verify(promotionApplicationFacade, times(2)).removeBookPromotion(anyLong());
        verify(promotionApplicationFacade).removeBookPromotion(101L);
        verify(promotionApplicationFacade).removeBookPromotion(102L);
    }
}
