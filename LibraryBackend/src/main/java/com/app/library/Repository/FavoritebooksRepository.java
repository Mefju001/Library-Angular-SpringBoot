package com.app.library.Repository;

import com.app.library.Entity.Book;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FavoritebooksRepository extends JpaRepository<Favoritebooks,Integer> {
    boolean existsByBookAndUser(Book book, User user);

    List<Favoritebooks> findFavoritebooksByUser_Id(Long userId);
    @Query("SELECT f.book.id FROM Favoritebooks f WHERE f.user.id = :userId")
    Set<Long> findBook_IdByUser_Id(Long userId);
}
