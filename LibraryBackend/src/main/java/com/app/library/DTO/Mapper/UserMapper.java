package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.UserResponse;
import com.app.library.Entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    UserResponse toDto(User user)
    {
        return new UserResponse(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().toString()
        );
    }

}
