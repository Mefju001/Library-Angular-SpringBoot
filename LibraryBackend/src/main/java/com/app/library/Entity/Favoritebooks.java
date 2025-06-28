package com.app.library.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "favoritebooksuser")
public class Favoritebooks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "books_id", nullable = false)
    private Book book;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Favoritebooks() {
    }

    public Favoritebooks(Book book, User user) {
        this.book = book;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFavoriteBook(Book book, User user) {
        setUser(user);
        setBook(book);
    }
}
