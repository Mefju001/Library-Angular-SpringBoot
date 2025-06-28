package com.app.library.Repository;

import com.app.library.Entity.BookImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookImgRepository extends JpaRepository<BookImg, Integer> {
    BookImg findBookImgByBook_Id(Integer bookId);
}
