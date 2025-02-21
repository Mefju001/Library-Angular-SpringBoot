package com.app.library.Controller;

import com.app.library.Entity.Favoritebooks;
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
    public ResponseEntity<List<Favoritebooks>> listoffavoritebooks() {
        return userService.findall();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Favoritebooks> addfavoritebooks(@RequestBody Favoritebooks favoritebooks) {
        return userService.addfavoritebooks(favoritebooks);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<Favoritebooks> updatefavoritebooks(@RequestBody Favoritebooks favoritebooks) {
        return userService.updatefavoritebooks(favoritebooks);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<Favoritebooks> deletefavoritebooks(@RequestParam Integer id) {
        return userService.deletefavoritebooks(id);
    }
}
