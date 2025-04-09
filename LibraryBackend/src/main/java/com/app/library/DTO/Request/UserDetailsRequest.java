package com.app.library.DTO.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsRequest {
    private String name;
    private String surname;
    private String email;
}
