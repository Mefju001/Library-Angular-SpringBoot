package com.app.library.DTO.Response;

import com.app.library.Entity.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
@Builder
public record UserResponse (Long id,String username,String password,String role){
}
