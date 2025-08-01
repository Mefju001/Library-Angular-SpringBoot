package com.app.library.Service;

import com.app.library.DTO.Request.UserDetailsRequest;
import com.app.library.DTO.Request.UserPasswordRequest;
import com.app.library.DTO.Response.FavoriteBooksResponse;
import com.app.library.DTO.Response.UserResponse;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Security.DTO.Request.UserRequest;
import com.app.library.Security.DTO.Response.JwtResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface UserService {
    List<FavoriteBooksResponse> findAllLikedBooks(Long userId);

    List<UserResponse> findAll();

    UserResponse findbyid(Long id);
    JwtResponse refreshToken(HttpServletRequest request, HttpServletResponse response);
    JwtResponse login(UserRequest loginRequest, HttpServletResponse response);
    Boolean hasAdminRole();
    void changedetails(Long id, UserDetailsRequest userRequest);

    void changepassword(Long id, UserPasswordRequest userPasswordRequest);

    void registerUp(UserRequest signUpRequest);

    void deleteuser(Long id);

    FavoriteBooksResponse addfavoritebooks(Integer bookId, Long userId);

    Favoritebooks updatefavoritebooks(Favoritebooks favoritebooks);

    void deletefavoritebooks(Integer id);

    Long getUserCount();
}
