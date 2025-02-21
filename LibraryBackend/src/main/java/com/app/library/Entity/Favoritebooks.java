package com.app.library.Entity;

import jakarta.persistence.*;
@Entity
@Table(name = "favoritebooks_user")
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
        private User User;

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

        public com.app.library.Entity.User getUser() {
                return User;
        }

        public void setUser(com.app.library.Entity.User user) {
                User = user;
        }
}
