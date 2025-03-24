package com.app.library.Repository;

import com.app.library.Entity.Author;
import com.app.library.Entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Integer> {
    Author findAuthorByNameAndSurname(String name, String surname);
}
