package com.app.library.Repository;

import com.app.library.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    public List<Book>findBooksByTitleContaining(String title);
    public Book findBookByIsbnIs(Long isbn);
    public List<Book>findBooksByAuthor_NameOrAuthor_Surname(String name,String Surname);
    public List<Book>findBooksByPriceIsBetween(float minprice,float maxprice);
    public List<Book>findBooksByGenreName(String name);
    public List<Book>findBooksByPublisherName(String name);
    public List<Book>findBooksByPublicationYearBetween(Integer year1,Integer year2);
}
