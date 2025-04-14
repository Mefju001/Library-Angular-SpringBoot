package com.app.library.DTO.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordRequest {
    private String oldpassword;
    private String newpassword;
    private String confirmpassword;
}
