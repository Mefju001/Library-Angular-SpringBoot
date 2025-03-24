package com.app.library.Repository;

import com.app.library.Entity.Genre;
import com.app.library.Entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher,Integer> {
    Publisher findPublisherByName(String name);
}
