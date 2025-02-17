package com.app.library.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Authors")
public class Author {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "Author_id")
        private Integer id;
        @Column(name = "Name")
        private String name;
        @Column(name = "Surname")
        private String surname;

        public Author() {
        }

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getSurname() {
                return surname;
        }

        public void setSurname(String surname) {
                this.surname = surname;
        }
}
