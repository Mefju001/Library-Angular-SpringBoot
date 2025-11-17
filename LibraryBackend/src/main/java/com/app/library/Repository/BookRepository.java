package com.app.library.Repository;

import com.app.library.Entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {
    Page<Book> findAll(Pageable pageable);

    List<Book> findAll();

    Page<Book> findBooksByTitleContaining(String title, Pageable pageable);
    Optional<Book> findBookByIsbn(long isbn);
    boolean existsBooksByIsbn(Long isbn);
    Page<Book> findBooksByAuthor_NameAndAuthor_Surname(String name, String Surname, Pageable pageable);
    Page<Book> findBooksByAuthor_NameOrAuthor_Surname(String name, String Surname, Pageable pageable);

    Page<Book> findBooksByPriceIsBetween(float minprice, float maxprice, Pageable pageable);

    Page<Book> findBooksByGenreName(String name, Pageable pageable);

    Page<Book> findBooksByPublisherName(String name, Pageable pageable);

    Page<Book> findBooksByPublicationDateBetween(LocalDate year1, LocalDate year2, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE FUNCTION('YEAR', b.publicationDate) = :publicationDateYear")
    Page<Book> findBooksByPublicationDateYear(@Param("publicationDateYear") int publicationDateYear, Pageable pageable);

    Page<Book> findBooksByPublicationDateIsGreaterThan(LocalDate year, Pageable pageable);

    Long countBooksByPublicationDateAfter(LocalDate todayDate);

}
