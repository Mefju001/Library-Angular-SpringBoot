package com.app.library.Service;

import com.app.library.DTO.Mapper.FavoriteBooksMapper;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.DTO.Response.FavoriteBooksResponse;
import com.app.library.DTO.Response.UserResponse;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Repository.FavoritebooksRepository;
import com.app.library.Service.Interfaces.BookService;
import com.app.library.Service.Interfaces.UserFavoritesService;
import com.app.library.Service.Interfaces.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
public class UserFavoritesServiceImpl implements UserFavoritesService {
    private final FavoritebooksRepository favoritebooksRepository;
    private final FavoriteBooksMapper favoriteBooksMapper;
    private final UserService userService;
    private final BookService bookService;

    public UserFavoritesServiceImpl(FavoritebooksRepository favoritebooksRepository, FavoriteBooksMapper favoriteBooksMapper, UserService userService, BookService bookService) {
        this.favoritebooksRepository = favoritebooksRepository;
        this.favoriteBooksMapper = favoriteBooksMapper;
        this.userService = userService;
        this.bookService = bookService;
    }

    @Override
    public List<FavoriteBooksResponse> findAllLikedBooks(Long userId) {
        return favoritebooksRepository.findFavoritebooksByUser_Id(userId).stream()
                .map(favoriteBooksMapper::toDto)
                .toList();
    }
    @Transactional
    @Override
    public FavoriteBooksResponse addfavoritebooks(Long isbn, Long userId) {
        var book = bookService.findByIsbn(isbn);
        var user = userService.getInternalUserEntity(userId);
        boolean exists = favoritebooksRepository.existsByBookAndUser(book, user);
        if (exists) {
            throw new IllegalArgumentException("is exist");
        }
        Favoritebooks favoritebooks = new Favoritebooks(book, user);
        favoritebooksRepository.save(favoritebooks);
        var bookResponse = new BookResponse(favoritebooks.getBook().getTitle(), favoritebooks.getBook().getAuthor().getName(), favoritebooks.getBook().getAuthor().getSurname(), favoritebooks.getBook().getpublicationDate(),
                favoritebooks.getBook().getIsbn(), favoritebooks.getBook().getGenre().getName(), favoritebooks.getBook().getLanguage(), favoritebooks.getBook().getPublisher().getName(), favoritebooks.getBook().getPages(), favoritebooks.getBook().getPrice());
        var userResponse = new UserResponse(favoritebooks.getUser().getUsername(),favoritebooks.getUser().getRoles().toString());
        return new FavoriteBooksResponse(bookResponse,userResponse);

    }

    @Transactional
    @Override
    public FavoriteBooksResponse updatefavoritebooks(Favoritebooks favoritebooks) {
        Favoritebooks existingdata = favoritebooksRepository.findById(favoritebooks.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        existingdata.setUser(existingdata.getUser());
        existingdata.setBook(existingdata.getBook());
        favoritebooksRepository.save(existingdata);
        return favoriteBooksMapper.toDto(favoritebooks);

    }

    @Transactional
    @Override
    public void deletefavoritebooks(Integer id) {
        Favoritebooks existingdata = favoritebooksRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found"));
        favoritebooksRepository.delete(existingdata);
    }
}
