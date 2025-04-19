package com.app.library.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "bookPromotions")
public class BookPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotions promotions;

    public BookPromotion() {

    }
    public BookPromotion(Book book, Promotions promotions) {
        this.book = book;
        this.promotions = promotions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Promotions getPromotions() {
        return promotions;
    }

    public void setPromotions(Promotions promotions) {
        this.promotions = promotions;
    }
}
