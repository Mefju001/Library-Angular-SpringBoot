package com.app.library.Service;

import com.app.library.Entity.Role;
import com.app.library.Security.DTO.Request.UserRequest;
import com.app.library.Security.DTO.Response.JwtResponse;
import com.app.library.Security.JWT.JwtUtils;
import com.app.library.Security.Service.UserDetailsImpl;
import com.app.library.Security.Service.UserDetailsServiceImpl;
import com.app.library.Service.Interfaces.AuthenticationService;
import com.app.library.Service.Interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    private final RoleService roleService;
    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService, UserDetailsServiceImpl userDetails, RoleService roleService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.userDetailsService = userDetails;
        this.roleService = roleService;
    }
    private String getRefreshToken(UserDetails userDetails) {
        return jwtUtils.generateRefreshToken(userDetails);
    }
    private ResponseCookie getResponseCookie(String refreshToken) {
        return ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(false) // Pamiętaj: true w produkcji!
                .path("/")
                .maxAge(jwtUtils.getJwtRefreshExpirationMs() / 1000)
                .sameSite("Lax")
                .build();
    }
    @Override
    public JwtResponse login(UserRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var refreshToken = getRefreshToken(userDetails);
        var refreshTokenCookie  = getResponseCookie(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        return new JwtResponse(jwt,
                refreshToken,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getAuthorities());

    }
    @Transactional
    @Override
    public void registerUp(UserRequest signUpRequest) {
        if (userService.checkIfUsernameExists(signUpRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.findRoleByName("ROLE_USER"));
        var results = userService.createUser(signUpRequest, roles);
        log.info("User {} has been registered successfully", results.username());
    }
    @Override
    public JwtResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String RefreshToken = null;
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    RefreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (RefreshToken == null || !jwtUtils.validateJwtToken(RefreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or missing refresh token.");
        }
        try {
            var username = jwtUtils.getUserNameFromJwtRefreshToken(RefreshToken);
            UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
            var newAccessToken = jwtUtils.generateTokenFromUsername(username,userDetails.getAuthorities());
            var newRefreshToken = getRefreshToken(userDetails);
            var newRefreshTokenCookie = getResponseCookie(newRefreshToken);
            response.addHeader(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString());
            return new JwtResponse(newAccessToken,
                    newRefreshToken,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getAuthorities());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or missing refresh token.");
        }
    }
    @Override
    public Boolean hasAdminRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null||authentication.getName().equals("anonymousUser")||!authentication.isAuthenticated())
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Collection<?extends GrantedAuthority> roles = authentication.getAuthorities();
        return roles.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }
}
