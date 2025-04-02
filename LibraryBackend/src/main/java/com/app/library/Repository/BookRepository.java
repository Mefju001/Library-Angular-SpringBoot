package com.app.library.Repository;

import com.app.library.Entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Page<Book> findAll(@NonNull Pageable pageable);
    Page<Book>findBooksByTitleContaining(String title,Pageable pageable);
    Book findBookByIsbnIs(Long isbn);
    Page<Book>findBooksByAuthor_NameOrAuthor_Surname(String name, String Surname,Pageable pageable);
    Page<Book>findBooksByPriceIsBetween(float minprice, float maxprice,Pageable pageable);
    Page<Book>findBooksByGenreName(String name,Pageable pageable);
    Page<Book>findBooksByPublisherName(String name,Pageable pageable);
    Page<Book>findBooksByPublicationYearBetween(Integer year1, Integer year2,Pageable pageable);
    Page<Book>findBooksByPublicationYearIs(Integer year,Pageable pageable);
    Page<Book>findBooksByPublicationYearIsGreaterThan(Integer year,Pageable pageable);


}
