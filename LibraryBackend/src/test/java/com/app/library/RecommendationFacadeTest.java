package com.app.library;

import com.app.library.DTO.Mapper.BookMapper;
import com.app.library.DTO.Mapper.DataReturn;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.Entity.*;
import com.app.library.Facade.RecommendationApplicationFacade;
import com.app.library.Facade.Services.Recommedation.FilterBooks;
import com.app.library.Facade.Services.Recommedation.RecommendationDataLookupService;
import com.app.library.Facade.Services.Recommedation.RecommendationScoreEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecommendationFacadeTest {
    @Spy
    private BookMapper bookMapper;
    @Mock
    private RecommendationScoreEngine recommendationScoreEngine;
    @Mock
    private RecommendationDataLookupService recommendationDataLookupService;
    private FilterBooks filterBooks;
    private RecommendationApplicationFacade recommendationApplicationFacade;
    @BeforeEach
    public void setup() {
        filterBooks = new FilterBooks(bookMapper,recommendationScoreEngine);
        filterBooks = spy(filterBooks);
        recommendationApplicationFacade = new RecommendationApplicationFacade(recommendationDataLookupService,filterBooks);
    }
    @Test
    void RecommendationsForUser()
    {
        var genre = new Genre();
        genre.setName("Genre");
        Publisher publisher = new Publisher();
        publisher.setId(1);
        var author = new Author();
        author.setId(1);
        List<Book> books = new ArrayList<>();
        List<Favoritebooks>favoritebooks = new ArrayList<>();
        var bookFan = new Book();
        bookFan.setId(1);
        favoritebooks.add(new Favoritebooks(bookFan,new User()));
        var bookRen = new Book();
        bookRen.setId(2);
        bookRen.setPublisher(publisher);
        bookRen.setAuthor(author);
        bookRen.setGenre(genre);
        List<Rental> rentals = new ArrayList<>();
        rentals.add(new Rental(bookRen,new User(), RentalStatus.loaned));
        var book = new Book();
        book.setId(3);
        book.setTitle("Title");
        book.setPublisher(publisher);
        book.setAuthor(author);
        book.setGenre(genre);
        books.add(book);
        books.add(bookFan);
        books.add(bookRen);
        var data = new DataReturn(favoritebooks,rentals,books);
        final var id = 1L;
        when(recommendationScoreEngine.computeScore(any(),anyList(),anyList())).thenReturn(0.8);
        when(recommendationDataLookupService.generateForUser(anyLong())).thenReturn(data);
        var result = recommendationApplicationFacade.RecommendationsForUser(id);
        assertEquals(1, result.size());
        assertEquals(book.getTitle(),result.iterator().next().title());
    }
}
