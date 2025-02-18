package com.app.library.Repository;

import com.app.library.Entity.Favoritebooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritebooksRepository extends JpaRepository<Favoritebooks,Integer> {
}
