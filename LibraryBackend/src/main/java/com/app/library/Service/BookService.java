package com.app.library.Service;

import com.app.library.Entity.Author;
import com.app.library.Entity.Book;
import com.app.library.Entity.Genre;
import com.app.library.Entity.Publisher;
import com.app.library.Repository.AuthorRepository;
import com.app.library.Repository.BookRepository;
import com.app.library.Repository.GenreRepository;
import com.app.library.Repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;


    @Autowired
    public BookService(BookRepository bookRepository, GenreRepository genreRepository, PublisherRepository publisherRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
    }


    public ResponseEntity<List<Book>> findall() {
        try {
            List<Book> books = bookRepository.findAll();
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Book>> findbooksbygenre(String name) {
        try {
            List<Book> books = bookRepository.findBooksByGenreName(name);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Book>> findbooksbypublisher(String name) {
        try {
            List<Book> books = bookRepository.findBooksByPublisherName(name);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Book>> findbooksbytitle(String title) {
        try {
            List<Book> books = bookRepository.findBooksByTitle(title);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Book>> findbooksbyauthor(String name, String surname) {
        List<Book> books = bookRepository.findBooksByAuthor_NameOrAuthor_Surname(name, surname);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    public ResponseEntity<List<Book>> findbooksbyprice(Float min, Float max) {
        List<Book> books = bookRepository.findBooksByPriceIsBetween(min, max);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    public ResponseEntity<Book> addbook(Book book) {
        try {
            // Sprawdzamy, czy książka już istnieje (na podstawie ISBN)
            if (bookRepository.findBookByIsbnIs(book.getIsbn()) != null) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT); // Konflikt, książka już istnieje
            }

            // Pobieramy lub tworzymy encje związane z książką (Genre, Author, Publisher)
            Genre existingGenre = genreRepository.findGenreByName(book.getGenre().getName());
            Author existingAuthor = authorRepository.findAuthorByNameAndSurname(book.getAuthor().getName(), book.getAuthor().getSurname());
            Publisher existingPublisher = publisherRepository.findPublisherByName(book.getPublisher().getName());

            // Jeśli encje nie istnieją, tworzymy je
            if (existingGenre == null) {
                existingGenre = new Genre();
                existingGenre.setName(book.getGenre().getName());
                existingGenre = genreRepository.save(existingGenre);
            }
            if (existingAuthor == null) {
                existingAuthor = new Author();
                existingAuthor.setName(book.getAuthor().getName());
                existingAuthor.setSurname(book.getAuthor().getSurname());
                existingAuthor = authorRepository.save(existingAuthor);
            }
            if (existingPublisher == null) {
                existingPublisher = new Publisher();
                existingPublisher.setName(book.getPublisher().getName());
                existingPublisher = publisherRepository.save(existingPublisher);
            }

            // Tworzymy nową książkę
            Book addbook = new Book();
            addbook.setTitle(book.getTitle());
            addbook.setPublication_year(book.getPublication_year());
            addbook.setIsbn(book.getIsbn());
            addbook.setLanguage(book.getLanguage());
            addbook.setPages(book.getPages());
            addbook.setPrice(book.getPrice());
            addbook.setGenre(existingGenre);
            addbook.setAuthor(existingAuthor);
            addbook.setPublisher(existingPublisher);

            // Zapisujemy książkę w bazie danych
            bookRepository.save(addbook);


            // Zwracamy odpowiedź z utworzoną książką
            //BookResponse bookResponse = new BookResponse(book);
            return new ResponseEntity<>(addbook, HttpStatus.CREATED);

        } catch (Exception e) {
            // W przypadku błędu zwracamy odpowiedź z błędem 409 (Konflikt)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<Book> updateBook(Book book){
        // Sprawdzamy, czy książka istnieje w bazie
        Book existingBook = bookRepository.findBookByIsbnIs(book.getIsbn());
        if (existingBook == null) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }


        // Aktualizujemy dane książki
        existingBook.setTitle(book.getTitle());
        existingBook.setPublication_year(book.getPublication_year());
        existingBook.setIsbn(book.getIsbn());
        existingBook.setLanguage(book.getLanguage());
        existingBook.setPages(book.getPages());
        existingBook.setPrice(book.getPrice());

        // Jeśli zmienił się gatunek, autor lub wydawca, aktualizujemy również te encje
        if (book.getGenre() != null) {
            Genre existingGenre = genreRepository.findGenreByName(book.getGenre().getName());
            existingBook.setGenre(existingGenre != null ? existingGenre : book.getGenre());
        }

        if (book.getAuthor() != null) {
            Author existingAuthor = authorRepository.findAuthorByNameAndSurname(book.getAuthor().getName(), book.getAuthor().getSurname());
            existingBook.setAuthor(existingAuthor != null ? existingAuthor : book.getAuthor());
        }

        if (book.getPublisher() != null) {
            Publisher existingPublisher = publisherRepository.findPublisherByName(book.getPublisher().getName());
            existingBook.setPublisher(existingPublisher != null ? existingPublisher : book.getPublisher());
        }
        bookRepository.save(existingBook);
        // Zapisujemy zaktualizowaną książkę w bazie danych
        return new ResponseEntity<>(existingBook,HttpStatus.OK);
    }
    public ResponseEntity<Book> deletebook(Integer id) {
        bookRepository.deleteById(id);
        if(bookRepository.findById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

