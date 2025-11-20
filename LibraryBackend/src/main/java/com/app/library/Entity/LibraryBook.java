package com.app.library.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "booklibrary")
public class LibraryBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    @ManyToOne
    @JoinColumn(name = "library_id", nullable = false)
    private Library library;
    private int Stock;

    public LibraryBook(Book book, Library library, int stock) {
        this.book = book;
        this.library = library;
        Stock = stock;
    }

    public LibraryBook(Library library, Book book) {
        this.library = library;
        this.book = book;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }
}
