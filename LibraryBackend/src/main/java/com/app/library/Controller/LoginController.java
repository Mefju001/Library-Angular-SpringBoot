package com.app.library.Controller;

import com.app.library.Security.DTO.Request.UserRequest;
import com.app.library.Security.DTO.Response.JwtResponse;
import com.app.library.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

    private final UserService userService;


    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.login(userRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest userRequest) {
        userService.registerUp(userRequest);
        return ResponseEntity.ok("Użytkownik został zarejestrowany!");
    }
}
