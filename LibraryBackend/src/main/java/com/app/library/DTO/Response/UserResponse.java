package com.app.library.DTO.Response;

import lombok.Builder;

//@Builder
public record UserResponse(String username, String password, String role) {
}
