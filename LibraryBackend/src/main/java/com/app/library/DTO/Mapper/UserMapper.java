package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.UserResponse;
import com.app.library.Entity.Role;
import com.app.library.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserMapper {
    UserResponse toDto(User user);

}
