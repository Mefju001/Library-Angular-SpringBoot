package com.app.library.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LibraryRequest {
    private Integer id;
    @NotBlank(message = "Library name cannot be blank")
    private String name;
    @NotBlank(message = "Library address cannot be blank")
    private String address;
}
