package com.app.library.Service;

import com.app.library.DTO.Response.BookResponse;
import com.app.library.Entity.Book;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Repository.FavoritebooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final FavoritebooksRepository favoritebooksRepository;
    @Autowired
    public UserService(FavoritebooksRepository favoritebooksRepository) {
        this.favoritebooksRepository = favoritebooksRepository;
    }
    public ResponseEntity<List<Favoritebooks>> findall() {
        try {
            List<Favoritebooks> favoritebooks = favoritebooksRepository.findAll();
            /*List<BookResponse>bookResponses = books.stream()
                    .map(bookMapper::toDto)
                    .toList();*/
            return new ResponseEntity<>(favoritebooks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
