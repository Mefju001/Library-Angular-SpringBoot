package com.app.library.Controller;

import com.app.library.DTO.Response.FavoriteBooksResponse;
import com.app.library.Service.Interfaces.UserFavoritesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ROLE_USER')")
@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "User Favorite Controller", description = "Zarządzanie polubionymi książkami")
public class UserFavoriteController {
    private final UserFavoritesService userFavoritesService;
    @Autowired
    public UserFavoriteController(UserFavoritesService userFavoritesService) {
        this.userFavoritesService = userFavoritesService;
    }
    @GetMapping("/")
    @Operation(summary = "Pobiera listę książek ulubionych użytkownika", description = "Zwraca listę ulubionych książek na podstawie ID użytkownika.")
    public ResponseEntity<List<FavoriteBooksResponse>> listoffavoritebooksByUserId(@Parameter(description = "ID użytkownika, którego ulubione książki mają zostać pobrane") @RequestParam Long userId) {
        List<FavoriteBooksResponse> favoriteBooks = userFavoritesService.findAllLikedBooks(userId);
        if (favoriteBooks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(favoriteBooks);
    }
    @PostMapping("/add")
    @Operation(summary = "Dodaje książkę do ulubionych użytkownika", description = "Dodaje książkę do ulubionych na podstawie ID książki i ID użytkownika.")
    public ResponseEntity<FavoriteBooksResponse> addfavoritebooks(@RequestParam Long isbn, @RequestParam Long userId) {
        FavoriteBooksResponse favoritebook = userFavoritesService.addfavoritebooks(isbn, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(favoritebook);
    }
    @DeleteMapping("/delete/favoritebook/{id}")
    @Operation(summary = "Usuwa książkę z ulubionych", description = "Usuwa książkę z ulubionych użytkownika.")
    public ResponseEntity<?> deletefavoritebooks(@PathVariable Integer id) {
        userFavoritesService.deletefavoritebooks(id);
        return ResponseEntity.noContent().build();
    }
}
