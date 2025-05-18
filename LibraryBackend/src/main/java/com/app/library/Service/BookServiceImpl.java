package com.app.library.Service;

import com.app.library.DTO.Mapper.BookMapper;
import com.app.library.DTO.Mapper.GenreMapper;
import com.app.library.DTO.Request.BookRequest;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.DTO.Response.GenreResponse;
import com.app.library.Entity.*;
import com.app.library.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    private final BookRepository bookRepository;
    private final AuditService auditService;
    private final BookImgRepository bookImgRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;
    private final GenreMapper genreMapper;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuditService auditService, BookImgRepository bookImgRepository, GenreRepository genreRepository, PublisherRepository publisherRepository, AuthorRepository authorRepository, BookMapper bookMapper, GenreMapper genreMapper) {
        this.bookRepository = bookRepository;
        this.auditService = auditService;
        this.bookImgRepository = bookImgRepository;
        this.genreRepository = genreRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
        this.genreMapper = genreMapper;
    }
    public Page<BookResponse> findall(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(bookMapper::toDto);
    }
    public BookResponse findbyid(Integer id) {
        Optional<Book> books = bookRepository.findById(id);
        return books.map(bookMapper::toDto).orElseThrow();
    }
    public BookImg findByBookId(Integer id)
    {
        BookImg bookImg = bookImgRepository.findBookImgByBook_Id(id);
        return bookImg;
    }
    public List<GenreResponse> findallgenres() {
            List<Genre> genres = genreRepository.findAll();
            return genres.stream()
                         .map(genreMapper::toDto)
                         .toList();
    }

    public Page<BookResponse> findbooksbygenre(String name,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findBooksByGenreName(name,pageable);
        return books.map(bookMapper::toDto);
    }

    public Page<BookResponse> findbooksbypublisher(String name,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findBooksByPublisherName(name,pageable);
        return books.map(bookMapper::toDto);
    }

    public Page<BookResponse> findbooksbytitle(String title,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findBooksByTitleContaining(title,pageable);
        return books.map(bookMapper::toDto);
    }

    public Page<BookResponse> findbooksbyauthor(String name, String surname,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findBooksByAuthor_NameOrAuthor_Surname(name, surname,pageable);
        return books.map(bookMapper::toDto);
    }

    public Page<BookResponse> findbooksbyprice(Float min, Float max,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findBooksByPriceIsBetween(min, max,pageable);
        return books.map(bookMapper::toDto);
    }
    public Page<BookResponse> findbooksbyyear(LocalDate year1, LocalDate year2,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findBooksByPublicationDateBetween(year1, year2,pageable);
        return books.map(bookMapper::toDto);
    }
    public Page<BookResponse> findnewbooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDate date = LocalDate.now();
        Page<Book> books = bookRepository.findBooksByPublicationDateYear(date.getYear(),pageable);
        return books.map(bookMapper::toDto);
    }

    @Override
    public Long getNewBooksCount() {
        return bookRepository.countBooksByPublicationDateAfter(LocalDate.now().minusMonths(1));
    }

    public Page<BookResponse> findforeshadowedbooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDate date = LocalDate.now();
        Page<Book> books = bookRepository.findBooksByPublicationDateIsGreaterThan(date,pageable);
        return books.map(bookMapper::toDto);
    }
    public Page<BookResponse> sortbooktitle(int page, int size,String type) {
        if (!type.equalsIgnoreCase("asc") && !type.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException("Nieprawidłowy typ sortowania: " + type);
        }

        Sort.Direction direction = type.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "title"));

        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(bookMapper::toDto);
    }
    public Page<BookResponse> sortbookprice(int page, int size,String type){
        if (!type.equalsIgnoreCase("asc") && !type.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException("Nieprawidłowy typ sortowania: " + type);
        }

        Sort.Direction direction = type.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "price"));

        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(bookMapper::toDto);
    }
    public Page<BookResponse> sortbookyear(int page, int size,String type){
        if (!type.equalsIgnoreCase("asc") && !type.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException("Nieprawidłowy typ sortowania: " + type);
        }

        Sort.Direction direction = type.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "publicationDate"));

        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(bookMapper::toDto);
    }

    private void setbook(Book book, BookRequest bookRequest){
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

    @Transactional
    public BookRequest addbook(BookRequest bookRequest) {
       if (bookRepository.findBookByIsbnIs(bookRequest.isbn()) != null) {
           System.out.println("Książka z tym ISBN już istnieje");
           logger.info("Książka z tym ISBN już istnieje");
       }
        Book newBook = new Book();
        setbook(newBook, bookRequest);
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        auditService.log("Post","Book",user,"Dodawanie ksiazki do bazy danych",newBook);
        return bookRequest;
    }
    @Transactional
    public BookRequest updateBook(Integer id,BookRequest bookRequest){
        Book Book = bookRepository.findById(id).orElseThrow(()->new RuntimeException("Książka nie znaleziona"));
        Book Book2 = new Book();
        BeanUtils.copyProperties(Book,Book2);
        setbook(Book, bookRequest);
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        auditService.logUpdate("Update","Book",user,Book2,Book);
        return bookRequest;
    }
    @Transactional
    public void deletebook(Integer id) {
        if(!bookRepository.existsById(id))
        {
            throw new EntityNotFoundException("Book not found with id like"+id);
        }
        Book deletedBook = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));
        bookRepository.deleteById(id);
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        auditService.log("Delete","Book",user,"Usuwanie ksiazki z bazy danych",deletedBook);
    }
}

