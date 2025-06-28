package com.app.library.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record UserDetailsRequest(
        @NotBlank(message = "Name cannot be blank") String name,
        @NotBlank(message = "Surname cannot be blank") String surname,
        @NotBlank(message = "Email cannot be blank") @Email(message = "Email must be valid") String email) {
}
