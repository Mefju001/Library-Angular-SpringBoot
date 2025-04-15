package com.app.library.Controller;

import com.app.library.DTO.Request.UserDetailsRequest;
import com.app.library.DTO.Request.UserPasswordRequest;
import com.app.library.DTO.Response.FavoriteBooksResponse;
import com.app.library.DTO.Response.UserResponse;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Service.RentalServiceImpl;
import com.app.library.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "User Controller", description = "Zarządzanie danymi użytkownika w aplikacji")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService, RentalServiceImpl rentalServiceImpl) {
        this.userService = userService;
    }
    @GetMapping("/")
    @Operation(summary = "Pobiera listę książek ulubionych użytkownika", description = "Zwraca listę ulubionych książek na podstawie ID użytkownika.")
    public ResponseEntity<List<FavoriteBooksResponse>> listoffavoritebooksByUserId(@Parameter(description = "ID użytkownika, którego ulubione książki mają zostać pobrane") @RequestParam Long userId) {
        List<FavoriteBooksResponse> favoriteBooks = userService.findall(userId);
        if (favoriteBooks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(favoriteBooks);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Zwraca użytkownika po ID", description = "Zwraca dane użytkownika na podstawie jego identyfikatora.")
    public ResponseEntity<UserResponse> userfindbyid(@PathVariable Long id) {
        UserResponse user = userService.findbyid(id);
        return ResponseEntity.ok(user);
    }
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    @Operation(summary = "Dodaje książkę do ulubionych użytkownika", description = "Dodaje książkę do ulubionych na podstawie ID książki i ID użytkownika.")
    public ResponseEntity<FavoriteBooksResponse> addfavoritebooks(@RequestParam Integer bookId,@RequestParam Long userId ) {
        FavoriteBooksResponse favoritebook = userService.addfavoritebooks(bookId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(favoritebook);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update")
    @Operation(summary = "Aktualizuje książkę w ulubionych", description = "Aktualizuje dane książki w ulubionych użytkownika.")
    public ResponseEntity<Favoritebooks> updatefavoritebooks(@RequestBody Favoritebooks favoritebooks) {
        Favoritebooks updatedBook = userService.updatefavoritebooks(favoritebooks);
        return ResponseEntity.ok(updatedBook);
    }
    @DeleteMapping("/delete/favoritebook/{id}")
    @Operation(summary = "Usuwa książkę z ulubionych", description = "Usuwa książkę z ulubionych użytkownika.")
    public ResponseEntity<?> deletefavoritebooks(@PathVariable Integer id) {
        userService.deletefavoritebooks(id);
        return ResponseEntity.noContent().build();
    }

    //user
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Usuwa użytkownika", description = "Usuwa użytkownika z bazy danych.")
    public ResponseEntity<?> deleteuser(@PathVariable Long id) {
        userService.deleteuser(id);
        return ResponseEntity.noContent().build();
    }
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/change/details/{id}")
    @Operation(summary = "Zmiana danych użytkownika", description = "Zmiana szczegółowych danych użytkownika.")
    public ResponseEntity<?> changedetails(@PathVariable Long id, @RequestBody UserDetailsRequest userDetailsRequest) {
        userService.changedetails(id, userDetailsRequest);
        return ResponseEntity.ok("Szczegóły zostały zaktualizowane.");
    }
    @PutMapping("/change/password/{id}")
    @Operation(summary = "Zmiana hasła użytkownika", description = "Zmienia hasło użytkownika na podstawie nowego hasła.")
    public ResponseEntity<?> changepassword(@PathVariable Long id, @RequestBody UserPasswordRequest userPasswordRequest) {
        userService.changepassword(id, userPasswordRequest);
        return ResponseEntity.ok("Hasło zostało zmienione.");
    }

}
