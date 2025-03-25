package com.app.library.Repository;

import com.app.library.Entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Page<Book> findAll(Pageable pageable);
    List<Book>findBooksByTitleContaining(String title);
    Book findBookByIsbnIs(Long isbn);
    List<Book>findBooksByAuthor_NameOrAuthor_Surname(String name, String Surname);
    List<Book>findBooksByPriceIsBetween(float minprice, float maxprice);
    List<Book>findBooksByGenreName(String name);
    List<Book>findBooksByPublisherName(String name);
    List<Book>findBooksByPublicationYearBetween(Integer year1, Integer year2);
}
