package com.app.library.Service;

import com.app.library.DTO.Mapper.BookMapper;
import com.app.library.DTO.Mapper.GenreMapper;
import com.app.library.DTO.Request.BookRequest;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.DTO.Response.GenreResponse;
import com.app.library.Entity.Author;
import com.app.library.Entity.Book;
import com.app.library.Entity.Genre;
import com.app.library.Entity.Publisher;
import com.app.library.Repository.AuthorRepository;
import com.app.library.Repository.BookRepository;
import com.app.library.Repository.GenreRepository;
import com.app.library.Repository.PublisherRepository;
import com.app.library.Security.DTO.Response.MessageResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {


    private static final Logger logger = LoggerFactory.getLogger(BookService.class);


    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;
    private final GenreMapper genreMapper;

    @Autowired
    public BookService(BookRepository bookRepository, GenreRepository genreRepository, PublisherRepository publisherRepository, AuthorRepository authorRepository, BookMapper bookMapper, GenreMapper genreMapper) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
        this.genreMapper = genreMapper;
    }


    public ResponseEntity<List<BookResponse>> findall() {
        try {
            List<Book> books = bookRepository.findAll();
            List<BookResponse>bookResponses = books.stream()
                                                    .map(bookMapper::toDto)
                                                    .toList();
            return new ResponseEntity<>(bookResponses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<BookResponse> findbyid(Integer id) {
        try {
            Optional<Book> books = bookRepository.findById(id);
            BookResponse bookResponse = books.map(bookMapper::toDto).orElseThrow();
            return new ResponseEntity<>(bookResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<GenreResponse>> findallgenres() {
        try {
            List<Genre> genres = genreRepository.findAll();
            List<GenreResponse>genreResponses = genres.stream()
                                                    .map(genreMapper::toDto)
                                                    .toList();
            return new ResponseEntity<>(genreResponses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<BookResponse>> findbooksbygenre(String name) {
        try {
            List<Book> books = bookRepository.findBooksByGenreName(name);
            List<BookResponse>bookResponses = books.stream()
                                                    .map(bookMapper::toDto)
                                                    .toList();
            return new ResponseEntity<>(bookResponses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<BookResponse>> findbooksbypublisher(String name) {
        try {
            List<Book> books = bookRepository.findBooksByPublisherName(name);
            List<BookResponse>bookResponses = books.stream()
                                                    .map(bookMapper::toDto)
                                                    .toList();
            return new ResponseEntity<>(bookResponses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<BookResponse>> findbooksbytitle(String title) {
        try {
            List<Book> books = bookRepository.findBooksByTitleContaining(title);
            List<BookResponse>bookResponses = books.stream()
                                                    .map(bookMapper::toDto)
                                                    .toList();
            return new ResponseEntity<>(bookResponses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<BookResponse>> findbooksbyauthor(String name, String surname) {
        List<Book> books = bookRepository.findBooksByAuthor_NameOrAuthor_Surname(name, surname);
        List<BookResponse>bookResponses = books.stream()
                                                .map(bookMapper::toDto)
                                                .toList();
        return new ResponseEntity<>(bookResponses, HttpStatus.OK);
    }

    public ResponseEntity<List<BookResponse>> findbooksbyprice(Float min, Float max) {
        List<Book> books = bookRepository.findBooksByPriceIsBetween(min, max);
        List<BookResponse>bookResponses = books.stream()
                                                .map(bookMapper::toDto)
                                                .toList();
        return new ResponseEntity<>(bookResponses, HttpStatus.OK);
    }
    public ResponseEntity<List<BookResponse>> findbooksbyyear(Integer year1, Integer year2) {
        List<Book> books = bookRepository.findBooksByPriceIsBetween(year1, year2);
        List<BookResponse>bookResponses = books.stream()
                                                .map(bookMapper::toDto)
                                                .toList();
        return new ResponseEntity<>(bookResponses, HttpStatus.OK);

    }
    @Transactional
    public ResponseEntity<BookRequest> addbook(BookRequest bookRequest) {
        try {
            // Sprawdzamy, czy książka już istnieje (na podstawie ISBN)
            if (bookRepository.findBookByIsbnIs(bookRequest.getIsbn()) != null) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT); // Konflikt, książka już istnieje
            }

            // Pobieramy lub tworzymy encje związane z książką (Genre, Author, Publisher)
            Genre existingGenre = genreRepository.findGenreByName(bookRequest.getGenreName());
            Author existingAuthor = authorRepository.findAuthorByNameAndSurname(bookRequest.getAuthorName(), bookRequest.getAuthorSurname());
            Publisher existingPublisher = publisherRepository.findPublisherByName(bookRequest.getPublisherName());

            // Jeśli encje nie istnieją, tworzymy je
            if (existingGenre == null) {
                existingGenre = new Genre();
                existingGenre.setName(bookRequest.getGenreName());
                existingGenre = genreRepository.save(existingGenre);
            }
            if (existingAuthor == null) {
                existingAuthor = new Author();
                existingAuthor.setName(bookRequest.getAuthorName());
                existingAuthor.setSurname(bookRequest.getAuthorSurname());
                existingAuthor = authorRepository.save(existingAuthor);
            }
            if (existingPublisher == null) {
                existingPublisher = new Publisher();
                existingPublisher.setName(bookRequest.getPublisherName());
                existingPublisher = publisherRepository.save(existingPublisher);
            }

            // Tworzymy nową książkę
            Book addbook = new Book();
            addbook.setTitle(bookRequest.getTitle());
            addbook.setPublicationYear(bookRequest.getPublicationYear());
            addbook.setIsbn(bookRequest.getIsbn());
            addbook.setLanguage(bookRequest.getLanguage());
            addbook.setPages(bookRequest.getPages());
            addbook.setPrice(bookRequest.getPrice());
            addbook.setGenre(existingGenre);
            addbook.setAuthor(existingAuthor);
            addbook.setPublisher(existingPublisher);

            // Zapisujemy książkę w bazie danych
            bookRepository.save(addbook);


            // Zwracamy odpowiedź z utworzoną książką
            //BookResponse bookResponse = new BookResponse(book);
            return new ResponseEntity<>(bookRequest, HttpStatus.CREATED);

        } catch (Exception e) {
            // W przypadku błędu zwracamy odpowiedź z błędem 409 (Konflikt)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }
    @Transactional
    public ResponseEntity<?> updateBook(Integer id,BookRequest bookRequest){
        // Sprawdzamy, czy książka istnieje w bazie
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        Book existingBook = bookRepository.findById(id).get();
        // Aktualizujemy dane książki
        existingBook.setTitle(bookRequest.getTitle());
        existingBook.setPublicationYear(bookRequest.getPublicationYear());
        existingBook.setIsbn(bookRequest.getIsbn());
        existingBook.setLanguage(bookRequest.getLanguage());
        existingBook.setPages(bookRequest.getPages());
        existingBook.setPrice(bookRequest.getPrice());

        // Jeśli zmienił się gatunek, autor lub wydawca, aktualizujemy również te encje
        if (bookRequest.getGenreName() != null) {
            Genre existingGenre = genreRepository.findGenreByName(bookRequest.getGenreName());
            if (existingGenre != null) {
                existingBook.setGenre(existingGenre);
                logger.info("aktualizacja genre");
            }
        }


        if (bookRequest.getAuthorName() != null || bookRequest.getAuthorSurname() != null) {
            Author existingAuthor = authorRepository.findAuthorByNameAndSurname(bookRequest.getAuthorName(), bookRequest.getAuthorSurname());
            if (existingAuthor != null) {
                existingBook.setAuthor(existingAuthor);
                logger.info("aktualizacja author");
            }
        }

        if (bookRequest.getPublisherName() != null) {
            Publisher existingPublisher = publisherRepository.findPublisherByName(bookRequest.getPublisherName());
            if (existingPublisher != null) {
                existingBook.setPublisher(existingPublisher);
                logger.info("aktualizacja publisher");
            }
        }
        bookRepository.save(existingBook);
        // Zapisujemy zaktualizowaną książkę w bazie danych
        return new ResponseEntity<>(bookRequest,HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> deletebook(Integer id) {
        bookRepository.deleteById(id);
        if(bookRepository.findById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

