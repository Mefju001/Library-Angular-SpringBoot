package com.app.library.Service;

import com.app.library.Entity.Author;
import com.app.library.Entity.Publisher;
import com.app.library.Repository.AuthorRepository;
import com.app.library.Repository.PublisherRepository;
import com.app.library.Service.Interfaces.RelationalEntityService;
import org.springframework.stereotype.Service;

@Service
public class RelationalEntityServiceImpl implements RelationalEntityService {
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;

    public RelationalEntityServiceImpl(PublisherRepository publisherRepository, AuthorRepository authorRepository) {
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
    }

    public Author getOrCreateAuthor(String name, String surname)
    {
        return authorRepository.findAuthorByNameAndSurname(name, surname)
                .orElseGet(() -> authorRepository.save(new Author(name, surname)));
    }
    public Publisher getOrCreatePublisher(String name)
    {
        return publisherRepository.findPublisherByName(name)
                .orElseGet(() -> publisherRepository.save(new Publisher(name)));
    }
}
