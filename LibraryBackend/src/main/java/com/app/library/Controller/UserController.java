package com.app.library.Controller;

import com.app.library.DTO.Request.UserDetailsRequest;
import com.app.library.DTO.Request.UserPasswordRequest;
import com.app.library.DTO.Response.BookResponse;
import com.app.library.DTO.Response.UserResponse;
import com.app.library.Security.Service.UserDetailsImpl;
import com.app.library.Service.Interfaces.RecommendationService;
import com.app.library.Service.Interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@PreAuthorize("hasRole('ROLE_USER')")
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "User Controller", description = "Zarządzanie danymi użytkownika w aplikacji")
public class UserController {
    private final UserService userService;
    private final RecommendationService recommendationService;

    @Autowired
    public UserController(UserService userService, RecommendationService recommendationService) {
        this.userService = userService;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Zwraca użytkownika po ID", description = "Zwraca dane użytkownika na podstawie jego identyfikatora.")
    public ResponseEntity<UserResponse> userfindbyid(@PathVariable Long id) {
        UserResponse user = userService.findbyid(id);
        return ResponseEntity.ok(user);
    }
    //user
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Usuwa użytkownika", description = "Usuwa użytkownika z bazy danych.")
    public ResponseEntity<?> deleteuser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteuser(userDetails.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/change/details/{id}")
    @Operation(summary = "Zmiana danych użytkownika", description = "Zmiana szczegółowych danych użytkownika.")
    public ResponseEntity<?> changedetails(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid UserDetailsRequest userDetailsRequest) {
        userService.changedetails(userDetails.getId(), userDetailsRequest);
        return ResponseEntity.ok("Szczegóły zostały zaktualizowane.");
    }

    @PutMapping("/change/password")
    @Operation(summary = "Zmiana hasła użytkownika", description = "Zmienia hasło użytkownika na podstawie nowego hasła.")
    public ResponseEntity<?> changepassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid UserPasswordRequest userPasswordRequest) {
        userService.changepassword(userDetails.getId(), userPasswordRequest);
        return ResponseEntity.ok("Hasło zostało zmienione.");
    }

    @GetMapping("/recommendation")
    @Operation(summary = "Generuje rekomendacje", description = "Zwraca zestaw rekomendowanych książek na podstawie preferencji zalogowanego użytkownika.")
    public ResponseEntity<Set<BookResponse>> recomend(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        var userId = userDetails.getId();
        Set<BookResponse> recom = recommendationService.generateForUser(userId);
        return ResponseEntity.ok(recom);
    }
}
