package com.app.library.Controller;

import com.app.library.Security.DTO.Request.UserRequest;
import com.app.library.Security.DTO.Response.JwtResponse;
import com.app.library.Service.Interfaces.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Auth Controller", description = "Logowanie i rejestracja do aplikacji")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    @Operation(summary = "Logowanie użytkownika", description = "Autentykacja użytkownika na podstawie loginu i hasła.")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody UserRequest userRequest,HttpServletResponse response) {
        return ResponseEntity.ok(authenticationService.login(userRequest,response));
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request,HttpServletResponse response){
        return ResponseEntity.ok(authenticationService.refreshToken(request,response));
    }
    @PostMapping("/register")
    @Operation(summary = "Rejestracja użytkownika", description = "Rejestracja nowego użytkownika. Wymaga podania danych użytkownika.")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest userRequest) {
        authenticationService.registerUp(userRequest);
        return ResponseEntity.ok("Użytkownik został zarejestrowany!");
    }
    @GetMapping("/has-role/admin")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> hasAdminRole() {
        return ResponseEntity.ok(authenticationService.hasAdminRole());
    }
}
