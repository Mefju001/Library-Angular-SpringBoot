package com.app.library.Service;
import com.app.library.Builder.BookBuilder;
import com.app.library.DTO.Mapper.BookMapper;
import com.app.library.DTO.MediatorRequest.AuditRequest;
import com.app.library.DTO.Request.BookCriteria;
import com.app.library.DTO.Request.BookRequest;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.Entity.*;
import com.app.library.Mediator.Mediator;
import com.app.library.Repository.*;
import com.app.library.Service.Interfaces.BookService;
import com.app.library.Service.Interfaces.GenreService;
import com.app.library.Service.Interfaces.RelationalEntityService;
import com.app.library.Service.QueryServices.BookQueryServices;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    private final Mediator mediator;
    private final BookBuilder  bookBuilder;
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookQueryServices bookQueryServices;
    private final GenreService genreService;
    private final BookImgRepository bookImgRepository;
    private final RelationalEntityService relationalEntityService;

    @Autowired
    public BookServiceImpl(Mediator mediator, BookRepository bookRepository, BookImgRepository bookImgRepository, BookQueryServices bookQueryServices, BookBuilder bookBuilder, BookMapper bookMapper, GenreService genreService, RelationalEntityService relationalEntityService) {
        this.mediator = mediator;
        this.bookRepository = bookRepository;
        this.bookImgRepository = bookImgRepository;
        this.bookQueryServices = bookQueryServices;
        this.bookBuilder = bookBuilder;
        this.bookMapper = bookMapper;
        this.genreService = genreService;
        this.relationalEntityService = relationalEntityService;
    }
    private String currentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    @Override
    public Book findByIsbn(Long isbn) {
        return bookRepository.findBookByIsbn(isbn).orElseThrow(EntityNotFoundException::new);
    }
    @Override
    public Page<BookResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(bookMapper::ToDto);
    }

    @Override
    public List<BookResponse> findAllList() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(bookMapper::ToDto).toList();
    }

    @Override
    public BookResponse findById(Integer id) {
        Optional<Book> books = bookRepository.findById(id);
        return books.map(bookMapper::ToDto).orElseThrow(()->new EntityNotFoundException("The book with ID "+ id + " was not found."));
    }

    @Override
    public BookImg findBookImgById(Integer id) {
        return bookImgRepository.findBookImgByBook_Id(id);
    }

    @Override
    public Long getNewBooksCount() {
        return bookRepository.countBooksByPublicationDateAfter(LocalDate.now().minusMonths(1));
    }
    private Book mapRequestToBook(BookRequest bookRequest) {
        return bookBuilder
                .CreateNewBook(bookRequest.title(),bookRequest.isbn(),bookRequest.price())
                .WithDetails(bookRequest.publicationDate(),bookRequest.language(),bookRequest.pages())
                .WithAuthor(relationalEntityService.getOrCreateAuthor(bookRequest.authorName(),bookRequest.authorSurname()))
                .WithGenre(genreService.getOrCreateGenre(bookRequest.genreName()))
                .WithPublisher(relationalEntityService.getOrCreatePublisher(bookRequest.publisherName()))
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
        newBook = bookRepository.save(newBook);
        mediator.send(new AuditRequest("Post", "Book", currentUser(), LocalDateTime.now(), "Dodawanie ksiazki do bazy danych", newBook));
        return bookMapper.ToDto(newBook);
    }

    @Override
    @Transactional
    public BookRequest updateBook(Integer id, BookRequest bookRequest) {
        var book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Książka nie znaleziona"));
        bookMapper.updateTheBook(book,bookRequest,
                genreService.getOrCreateGenre(bookRequest.genreName()),
                relationalEntityService.getOrCreatePublisher(bookRequest.publisherName()),
                relationalEntityService.getOrCreateAuthor(bookRequest.authorName(), bookRequest.authorSurname()));
        bookRepository.save(book);
        mediator.send(new AuditRequest("Update", "Book", currentUser(), LocalDateTime.now(), "Aktualizacja istniejącego elementu w bazie danych", book));
        return bookRequest;
    }

    @Override
    @Transactional
    public void deletebook(Integer id) {
        var deletedBook = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));
        bookRepository.delete(deletedBook);
        mediator.send(new AuditRequest("Delete", "Book", currentUser(), LocalDateTime.now(), "Usuwanie ksiazki z bazy danych", deletedBook));
    }

    @Override
    public Page<BookResponse> searchOrSortBooksByCriteria(BookCriteria criteria) {
        if(criteria == null){
            throw new IllegalArgumentException("Criteria cannot be null");
        }
        return bookQueryServices.FindBooksByCriteria(criteria);
    }
}

