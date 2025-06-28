package com.app.library.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


public record UserPasswordRequest(
        @NotBlank(message = "Old password cannot be blank")String oldpassword,
        @NotBlank(message = "New password cannot be blank")
        @Size(min = 8, message = "New password must be at least 8 characters long")

        @Pattern(regexp = ".*[A-Z].*", message = "New password must contain at least one uppercase letter")
        @Pattern(regexp = ".*\\d.*", message = "New password must contain at least one number")
        String newpassword,
        @NotBlank(message = "Confirm password cannot be blank") String confirmpassword)
{
}
