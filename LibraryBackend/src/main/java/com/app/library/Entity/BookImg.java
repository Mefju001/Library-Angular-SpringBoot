package com.app.library.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bookimg")
public class BookImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "book_img")
    private String bookImg;
    @OneToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    public BookImg() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookImg() {
        return bookImg;
    }

    public void setBookImg(String bookImg) {
        this.bookImg = bookImg;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
