package com.app.library;

import com.app.library.Builder.Review.ReviewBuilder;
import com.app.library.DTO.Mapper.BookMapper;
import com.app.library.DTO.Mapper.ReviewMapper;
import com.app.library.DTO.Mapper.UserMapper;
import com.app.library.DTO.Request.ReviewRequest;
import com.app.library.Entity.*;
import com.app.library.Repository.ReviewRepository;
import com.app.library.Service.Interfaces.BookService;
import com.app.library.Service.Interfaces.RentalService;
import com.app.library.Service.Interfaces.UserService;
import com.app.library.Service.ReviewServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest
{
    @Spy
    private ReviewBuilder reviewBuilder = new ReviewBuilder();
    @Mock
    private ReviewRepository reviewRepository;
    @Spy
    private UserMapper userMapper = new UserMapper();
    @Spy
    private BookMapper bookMapper = new BookMapper();
    @Spy
    private ReviewMapper reviewMapper = new ReviewMapper(userMapper, bookMapper);
    @Mock
    private UserService userService;
    @Mock
    private BookService bookService;
    @InjectMocks
    private ReviewServiceImpl reviewServiceImpl;
    @Test
    void add()
    {
        var user = new User();
        user.setId(1L);
        var book = new Book();
        book.setAuthor(new Author());
        book.setGenre(new Genre());
        book.setPublisher(new Publisher());
        book.setId(1);
        doReturn(user).when(userService).getInternalUserEntity(user.getId());
        doReturn(book).when(bookService).getInternalBookEntity(book.getId());
        when(reviewRepository.existsByUser_IdAndBook_Id(anyLong(),anyInt())).thenReturn(false);
        var reviewRequest = new ReviewRequest("content",5,LocalDate.now(), user.getId(), book.getId());
        var review = new Review();
        review.setContent("content");
        review.setBook(book);
        review.setUser(user);
        review.setCreatedAt(LocalDate.now());
        review.setRating(5);
        when(reviewRepository.save(any())).thenReturn(review);
        var result = reviewServiceImpl.add(reviewRequest);
        verify(reviewRepository, times(1)).save(any());
        verify(reviewRepository).existsByUser_IdAndBook_Id(anyLong(),anyInt());
        verify(bookService, times(1)).getInternalBookEntity(book.getId());
        verify(reviewMapper, times(1)).toDto(any());
        verify(userService, times(1)).getInternalUserEntity(user.getId());
    }
}
