package com.app.library.Service;
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
    private final BookRepository bookRepository;
    private final BookQueryServices bookQueryServices;
    //Mediator zamiast dostępu    private final AuditService auditService;
    private final BookImgRepository bookImgRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;
    private final GenreMapper genreMapper;
    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookQueryServices bookQueryService, BookImgRepository bookImgRepository, GenreRepository genreRepository, PublisherRepository publisherRepository, AuthorRepository authorRepository, BookMapper bookMapper, GenreMapper genreMapper, BookQueryServices bookQueryServices) {
        this.bookRepository = bookRepository;
        bookQueryServices = bookQueryService;
        //this.auditService = auditService;
        this.bookImgRepository = bookImgRepository;
        this.genreRepository = genreRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
        this.genreMapper = genreMapper;
        this.bookQueryServices = bookQueryServices;
    }

    @Override
    public Page<BookResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(BookMapper::ToBookResponse);
    }

    @Override
    public List<BookResponse> findAllList() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(BookMapper::ToBookResponse).toList();
    }

    @Override
    public BookResponse findById(Integer id) {
        Optional<Book> books = bookRepository.findById(id);
        return books.map(BookMapper::ToBookResponse).orElseThrow(()->new EntityNotFoundException("The book with ID "+ id + " was not found."));
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

    private void setbook(Book book, BookRequest bookRequest) {
        book.setTitle(bookRequest.title());
        book.setpublicationDate(bookRequest.publicationDate());
        book.setIsbn(bookRequest.isbn());
        book.setLanguage(bookRequest.language());
        book.setPages(bookRequest.pages());
        book.setPrice(bookRequest.price());
        book.setOldprice(bookRequest.price());
        book.setGenre(getOrCreateGenre(bookRequest.genreName()));
        book.setAuthor(getOrCreateAuthor(bookRequest.authorName(), bookRequest.authorSurname()));
        book.setPublisher(getOrCreatePublisher(bookRequest.publisherName()));
        bookRepository.save(book);
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

    @Override
    @Transactional
    public BookRequest addbook(BookRequest bookRequest) {
        if (bookRepository.findBookByIsbnIs(bookRequest.isbn()) != null) {
            System.out.println("Książka z tym ISBN już istnieje");
            logger.info("Książka z tym ISBN już istnieje");
        }
        Book newBook = new Book();
        setbook(newBook, bookRequest);
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        //auditService.log("Post", "Book", user, "Dodawanie ksiazki do bazy danych", newBook);
        return bookRequest;
    }

    @Override
    @Transactional
    public BookRequest updateBook(Integer id, BookRequest bookRequest) {
        Book Book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Książka nie znaleziona"));
        Book Book2 = new Book();
        BeanUtils.copyProperties(Book, Book2);
        setbook(Book, bookRequest);
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        //auditService.logUpdate("Update", "Book", user, Book2, Book);
        return bookRequest;
    }

    @Override
    @Transactional
    public void deletebook(Integer id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found with id like" + id);
        }
        Book deletedBook = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));
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

