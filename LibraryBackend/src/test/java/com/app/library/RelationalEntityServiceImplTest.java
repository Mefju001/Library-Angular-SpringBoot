package com.app.library;

import com.app.library.Entity.Author;
import com.app.library.Entity.Publisher;
import com.app.library.Repository.AuthorRepository;
import com.app.library.Repository.PublisherRepository;
import com.app.library.Service.RelationalEntityServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RelationalEntityServiceImplTest {
    @Mock
    private PublisherRepository publisherRepository;
    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private RelationalEntityServiceImpl relationalEntityService;
    @Test
    void getOrCreateAuthor()
    {
        var author = new Author();
        author.setId(1);
        author.setName("name");
        author.setSurname("surname");
        when(authorRepository.findAuthorByNameAndSurname(anyString(), anyString())).thenReturn(Optional.empty());
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        var results = relationalEntityService.getOrCreateAuthor("name", "surname");
        assertEquals(author.getName(),results.getName());
        assertEquals(author.getSurname(),results.getSurname());
    }
    @Test
    void getOrCreatePublisher()
    {
        var publisher = new Publisher();
        publisher.setId(1);
        publisher.setName("name");
        when(publisherRepository.findPublisherByName(anyString())).thenReturn(Optional.empty());
        when(publisherRepository.save(any(Publisher.class))).thenReturn(publisher);
        var result = relationalEntityService.getOrCreatePublisher("name");
        assertEquals(publisher.getName(),result.getName());
    }
}
