package com.app.library.Repository;

import com.app.library.Entity.Genre;
import com.app.library.Entity.LibraryBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryBookRepository extends JpaRepository<LibraryBook,Integer> {
    List<LibraryBook> findLibraryBookByBook_Title(String title);

}
