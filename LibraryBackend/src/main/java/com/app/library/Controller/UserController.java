package com.app.library.Controller;

import com.app.library.DTO.Request.UserDetailsRequest;
import com.app.library.DTO.Request.UserPasswordRequest;
import com.app.library.DTO.Response.FavoriteBooksResponse;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Entity.User;
import com.app.library.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/")
    public ResponseEntity<List<FavoriteBooksResponse>> listoffavoritebooksByUserId(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.findall(userId));
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> userfindbyid(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findbyid(id));
    }
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Favoritebooks> addfavoritebooks(@RequestParam Integer bookId,@RequestParam Long userId ) {
        return ResponseEntity.ok(userService.addfavoritebooks(bookId,userId));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<Favoritebooks> updatefavoritebooks(@RequestBody Favoritebooks favoritebooks) {
        return ResponseEntity.ok(userService.updatefavoritebooks(favoritebooks));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/favoritebook")
    public ResponseEntity<?> deletefavoritebooks(@RequestParam Integer id) {
        userService.deletefavoritebooks(id);
        return ResponseEntity.ok("Usunieto");
    }

    //user
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteuser(@PathVariable Long id) {
        userService.deleteuser(id);
        return ResponseEntity.ok("Usunieto");
    }
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/change/details/{id}")
    public ResponseEntity<?> changedetails(@PathVariable Long id, @RequestBody UserDetailsRequest userDetailsRequest) {
        userService.changedetails(id, userDetailsRequest);
        return ResponseEntity.ok("Zmieniono");
    }
    @PutMapping("/change/password/{id}")
    public ResponseEntity<?> changepassword(@PathVariable Long id, @RequestBody UserPasswordRequest userPasswordRequest) {
        userService.changepassword(id,userPasswordRequest);
        return ResponseEntity.ok("Zmieniono");
    }

}
