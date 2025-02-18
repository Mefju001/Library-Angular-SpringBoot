package com.app.library.Entity;

import jakarta.persistence.*;
@Entity
@Table(name = "favoritebooks_user")
public class Favoritebooks {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;
        @ManyToOne
        @JoinColumn(name = "books_id", nullable = false)
        private Book book;
        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User User;

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

        public com.app.library.Entity.User getUser() {
                return User;
        }

        public void setUser(com.app.library.Entity.User user) {
                User = user;
        }
}
