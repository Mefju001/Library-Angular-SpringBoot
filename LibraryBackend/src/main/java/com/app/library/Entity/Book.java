package com.app.library.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "Books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
    @Column(name = "publication")
    private LocalDate publicationDate;
    @Column(name = "isbn")
    private long isbn;
    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;
    @Column(name = "language")
    private String language;
    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;
    @Column(name = "pages")
    private int pages;
    @Column(name = "price")
    private float price;
    @Column(name = "oldprice")
    private Float oldprice;
    public Book() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public LocalDate getpublicationDate() {
        return publicationDate;
    }

    public void setpublicationDate(LocalDate publication) {
        this.publicationDate = publication;
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public float getOldprice() {
        return oldprice;
    }

    public void setOldprice(float oldprice) {
        this.oldprice = oldprice;
    }
}
