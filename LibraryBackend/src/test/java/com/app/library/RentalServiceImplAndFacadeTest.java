package com.app.library;

import com.app.library.DTO.Mapper.BookMapper;
import com.app.library.DTO.Mapper.RentalBookMapper;
import com.app.library.DTO.Mapper.UserMapper;
import com.app.library.Entity.*;
import com.app.library.EventListener.RentalCreatedEvent;
import com.app.library.Facade.RentalProcessingFacade;
import com.app.library.Facade.ReturnProcessingFacade;
import com.app.library.Facade.Services.PenaltyCalculationService;
import com.app.library.Facade.Services.Rental.RentalPersistenceService;
import com.app.library.Facade.Services.Rental.ReturnRentalPersistenceService;
import com.app.library.Repository.RentalRepository;
import com.app.library.Service.RentalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RentalServiceImplAndFacadeTest {
    private RentalProcessingFacade rentalProcessingFacade;
    private RentalServiceImpl rentalService;
    private ReturnProcessingFacade returnProcessingFacade;
    @Mock
    private ReturnRentalPersistenceService returnRentalPersistenceService;
    @Mock
    private ApplicationEventPublisher publisher;
    @Mock
    private PenaltyCalculationService penaltyCalculationService;
    @Mock
    private RentalPersistenceService persistenceService;
    @Mock
    private RentalRepository rentalRepository;
    @Spy
    private UserMapper userMapper =  new UserMapper();
    @Spy
    private BookMapper bookMapper =  new BookMapper();
    @Spy
    private RentalBookMapper rentalBookMapper = new RentalBookMapper(userMapper, bookMapper);
    @BeforeEach
    public void setUp() {
        rentalProcessingFacade = spy(new RentalProcessingFacade(persistenceService, publisher));
        returnProcessingFacade = new ReturnProcessingFacade(returnRentalPersistenceService,publisher,penaltyCalculationService);
        rentalService = new RentalServiceImpl(rentalRepository,returnProcessingFacade,rentalBookMapper,rentalProcessingFacade);
    }
    private void setAuth()
    {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(authentication.getName()).thenReturn("test-user");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
    private Rental createRental() {
        var rental = new Rental();
        var user = new User();
        var book = new Book();
        var author = new Author();
        var genre = new Genre();
        var publisher = new Publisher();
        book.setId(1);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setPublisher(publisher);
        rental.setUser(user);
        rental.setBook(book);
        rental.setRentalStartDate(LocalDate.now());
        rental.setRentalEndDate(LocalDate.now().plusMonths(3));
        rental.setReturnRequestDate(LocalDate.now());
        rental.setStatus(RentalStatus.loaned);
        return rental;
    }
    @Test
    void rentalList()
    {
        final Long id = 1L;
        var rental = createRental();
        rental.setRentalId(1);
        List<Rental> rentals = List.of(rental);
        when(rentalRepository.findRentalsByUser_Id(id)).thenReturn(rentals);
        var result = rentalService.rentalList(id);
        verify(rentalRepository).findRentalsByUser_Id(id);
        assertEquals(rentals.size(),result.size());
        assertEquals(rental.getRentalId(),result.getFirst().rentalId());
    }
    @Test
    void requestRentABook()
    {
        final int bookId = 1;
        final Long userId = 1L;
        var rental = createRental();
        when(persistenceService.createPendingRental(anyInt(),anyLong())).thenReturn(rental);
        rentalService.requestRentABook(bookId,userId);
        verify(publisher).publishEvent(any(RentalCreatedEvent.class));
    }
    @Test
    void requestReturn()
    {
        final int bookId = 1;
        final Long userId = 1L;
        var rental = createRental();
        when(returnRentalPersistenceService.requestForReturnABook(anyInt(),anyLong())).thenReturn(rental);
        rentalService.requestReturn(bookId,userId);
        verify(publisher).publishEvent(any(RentalCreatedEvent.class));
    }
    @Test
    void howManyDaysLeft()
    {
        final int bookId = 1;
        final Long userId = 1L;
        var rental = createRental();
        LocalDate today = LocalDate.now();
        LocalDate endDate = rental.getRentalEndDate();
        long daysBetween = ChronoUnit.DAYS.between(today, endDate);
        var overdue = daysBetween < 0;
        when(rentalRepository.findRentalByBook_IdAndUser_Id(anyInt(),anyLong())).thenReturn(Optional.of(rental));
        var result = rentalService.howManyDaysLeft(bookId,userId);
        assertEquals(daysBetween,result.days());
        assertEquals(overdue,result.isOverdue());
        verify(rentalRepository).findRentalByBook_IdAndUser_Id(anyInt(),anyLong());
    }
    @Test
    void requestExtendLoan()
    {
        final int bookId = 1;
        final Long userId = 1L;
        rentalService.requestExtendLoan(bookId,userId);
        verify(persistenceService).requestExtendLoan(anyInt(),anyLong());
        verify(rentalProcessingFacade).requestExtendRent(anyInt(),anyLong());
    }
    @Test
    void approveExtenLoan()
    {
        final int bookId = 1;
        setAuth();
        rentalService.approveExtendLoan(bookId);
        verify(persistenceService).approveExtend(anyInt(),anyString());
        verify(rentalProcessingFacade).approveExtendRent(anyInt(),anyString());
    }
    @Test
    void cancelLoanBook()
    {
        final int bookId = 1;
        final Long userId = 1L;
        rentalService.cancelLoanBook(bookId,userId);
        verify(persistenceService).cancelLoanABook(anyInt(),anyLong());
        verify(rentalProcessingFacade).cancelRentBook(anyInt(),anyLong());
    }
    @Test
    void getActiveBorrowsCount()
    {
        when(rentalRepository.countByStatusIn(any())).thenReturn(1L);
        var result = rentalService.getActiveBorrowsCount();
        verify(rentalRepository).countByStatusIn(any());
        assertEquals(1,result);
    }
    @Test
    void getOverdueCount()
    {
        when(rentalRepository.countByStatusIn(any())).thenReturn(2L);
        var result = rentalService.getOverdueCount();
        verify(rentalRepository).countByStatusIn(any());
        assertEquals(2,result);
    }
}
