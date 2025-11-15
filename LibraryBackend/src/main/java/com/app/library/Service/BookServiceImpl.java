package com.app.library.Service;
import com.app.library.Builder.BookBuilder;
import com.app.library.DTO.Mapper.BookMapper;
import com.app.library.DTO.Mapper.GenreMapper;
import com.app.library.DTO.Request.BookCriteria;
import com.app.library.DTO.Request.BookRequest;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.DTO.Response.GenreResponse;
import com.app.library.Entity.*;
import com.app.library.Repository.*;
import com.app.library.Service.Interfaces.BookService;
import com.app.library.Service.QueryServices.BookQueryServices;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    private final BookBuilder  bookBuilder;
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookQueryServices bookQueryServices;
    //Mediator zamiast dostępu    private final AuditService auditService;
    private final BookImgRepository bookImgRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final GenreMapper genreMapper;
    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookImgRepository bookImgRepository, GenreRepository genreRepository, PublisherRepository publisherRepository, AuthorRepository authorRepository, GenreMapper genreMapper, BookQueryServices bookQueryServices, BookBuilder bookBuilder, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        //this.auditService = auditService;
        this.bookImgRepository = bookImgRepository;
        this.genreRepository = genreRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
        this.genreMapper = genreMapper;
        this.bookQueryServices = bookQueryServices;
        this.bookBuilder = bookBuilder;
        this.bookMapper = bookMapper;
    }

    @Override
    public Page<BookResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(bookMapper::ToBookResponse);
    }

    @Override
    public List<BookResponse> findAllList() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(bookMapper::ToBookResponse).toList();
    }

    @Override
    public BookResponse findById(Integer id) {
        Optional<Book> books = bookRepository.findById(id);
        return books.map(bookMapper::ToBookResponse).orElseThrow(()->new EntityNotFoundException("The book with ID "+ id + " was not found."));
    }

    @Override
    public BookImg findBookImgById(Integer id) {
        return bookImgRepository.findBookImgByBook_Id(id);
    }

    @Override
    public List<GenreResponse> findallgenres() {
        List<Genre> genres = genreRepository.findAll();
        return genres.stream()
                .map(genreMapper::toDto)
                .toList();
    }

    @Override
    public Long getNewBooksCount() {
        return bookRepository.countBooksByPublicationDateAfter(LocalDate.now().minusMonths(1));
    }

    private Genre getOrCreateGenre(String name) {
        return genreRepository.findGenreByName(name)
                .orElseGet(() -> genreRepository.save(new Genre(name)));
    }

    private Author getOrCreateAuthor(String name, String surname) {
        return authorRepository.findAuthorByNameAndSurname(name, surname)
                .orElseGet(() -> authorRepository.save(new Author(name, surname)));
    }

    private Publisher getOrCreatePublisher(String name) {
        return publisherRepository.findPublisherByName(name)
                .orElseGet(() -> publisherRepository.save(new Publisher(name)));
    }
    private Book mapRequestToBook(BookRequest bookRequest) {
        return bookBuilder
                .CreateNewBook(bookRequest.title(),bookRequest.isbn(),bookRequest.price())
                .WithDetails(bookRequest.publicationDate(),bookRequest.language(),bookRequest.pages())
                .WithAuthor(getOrCreateAuthor(bookRequest.authorName(),bookRequest.authorSurname()))
                .WithGenre(getOrCreateGenre(bookRequest.genreName()))
                .WithPublisher(getOrCreatePublisher(bookRequest.publisherName()))
                .build();
    }
    @Override
    @Transactional
    public BookResponse addbook(BookRequest bookRequest) {
        if (bookRepository.existsBooksByIsbn(bookRequest.isbn())) {
            logger.info("Książka z tym ISBN już istnieje");
            throw new IllegalArgumentException();
        }
        var newBook = mapRequestToBook(bookRequest);
        bookRepository.save(newBook);
        //String user = SecurityContextHolder.getContext().getAuthentication().getName();
        //auditService.log("Post", "Book", user, "Dodawanie ksiazki do bazy danych", newBook);
        return bookMapper.ToBookResponse(newBook);
    }

    @Override
    @Transactional
    public BookRequest updateBook(Integer id, BookRequest bookRequest) {
        var book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Książka nie znaleziona"));
        bookMapper.updateTheBook(book,bookRequest,
                getOrCreateGenre(bookRequest.genreName()),
                getOrCreatePublisher(bookRequest.publisherName()),
                getOrCreateAuthor(bookRequest.authorName(), bookRequest.authorSurname()));
        bookRepository.save(book);
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        //auditService.logUpdate("Update", "Book", user, Book2, Book);
        return bookRequest;
    }

    @Override
    @Transactional
    public void deletebook(Integer id) {
        bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));
        bookRepository.deleteById(id);
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        //auditService.log("Delete", "Book", user, "Usuwanie ksiazki z bazy danych", deletedBook);
    }

    @Override
    public Page<BookResponse> searchOrSortBooksByCriteria(BookCriteria criteria) {
        if(criteria == null){
            throw new IllegalArgumentException("Criteria cannot be null");
        }
        return bookQueryServices.FindBooksByCriteria(criteria);
    }
}

