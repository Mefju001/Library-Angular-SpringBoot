package com.app.library.Service.Interfaces;

import com.app.library.Security.DTO.Request.UserRequest;
import com.app.library.Security.DTO.Response.JwtResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

public interface AuthenticationService {
    JwtResponse login(UserRequest loginRequest, HttpServletResponse response);

    @Transactional
    void registerUp(UserRequest signUpRequest);

    JwtResponse refreshToken(HttpServletRequest request, HttpServletResponse response);

    Boolean hasAdminRole();
}
