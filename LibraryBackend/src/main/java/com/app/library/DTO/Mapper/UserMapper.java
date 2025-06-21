package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.BookResponse;
import com.app.library.DTO.Response.UserResponse;
import com.app.library.Entity.Book;
import com.app.library.Entity.Role;
import com.app.library.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "Spring")
public interface UserMapper {
    @Mapping(target = "role", expression = "java(mapRole(user.getRoles()))")

    UserResponse toDto(User user);
    default String mapRole(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) return null;
        return roles.iterator().next().getRola();
    }
}
