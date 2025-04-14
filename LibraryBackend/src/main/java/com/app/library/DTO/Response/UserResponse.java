package com.app.library.DTO.Response;

import com.app.library.Entity.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
@Getter
@Builder
public class UserResponse {
    private final Long id;
    private final String username;
    private final String password;
    private final String role;
}
