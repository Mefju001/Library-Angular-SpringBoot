package com.app.library.Repository;

import com.app.library.Entity.Book;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritebooksRepository extends JpaRepository<Favoritebooks,Integer> {
    boolean existsByBookAndUser(Book book, User user);

    List<Favoritebooks> findFavoritebooksByUser_Id(Long userId);
}
