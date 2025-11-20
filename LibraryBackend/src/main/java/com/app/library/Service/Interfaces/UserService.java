package com.app.library.Service.Interfaces;

import com.app.library.DTO.Request.UserDetailsRequest;
import com.app.library.DTO.Request.UserPasswordRequest;
import com.app.library.DTO.Response.FavoriteBooksResponse;
import com.app.library.DTO.Response.UserResponse;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Entity.Role;
import com.app.library.Entity.User;
import com.app.library.Security.DTO.Request.UserRequest;
import com.app.library.Security.DTO.Response.JwtResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserResponse createUser(UserRequest signUpRequest, Set<Role> roles);
    List<UserResponse> findAll();
    boolean checkIfUsernameExists(String username);
    User findByUsername(String name);
    UserResponse findbyid(Long id);
    void changedetails(Long id, UserDetailsRequest userRequest);
    User getInternalUserEntity(Long id);
    void changepassword(Long id, UserPasswordRequest userPasswordRequest);
    void deleteuser(Long id);
    Long getUserCount();
}
