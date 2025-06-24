package com.app.library.Controller;

import com.app.library.Security.DTO.Request.UserRequest;
import com.app.library.Security.DTO.Response.JwtResponse;
import com.app.library.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Login Controller", description = "Logowanie i rejestracja do aplikacji")
public class LoginController {

    private final UserService userService;


    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "Logowanie użytkownika", description = "Autentykacja użytkownika na podstawie loginu i hasła.")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.login(userRequest));
    }

    @PostMapping("/register")
    @Operation(summary = "Rejestracja użytkownika", description = "Rejestracja nowego użytkownika. Wymaga podania danych użytkownika.")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest userRequest) {
        userService.registerUp(userRequest);
        return ResponseEntity.ok("Użytkownik został zarejestrowany!");
    }
}
