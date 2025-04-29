package com.app.library.DTO.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LibraryRequest {
    private Integer id;
    @NotBlank(message = "Library location cannot be blank")
    private String location;
    @NotBlank(message = "Library address cannot be blank")
    private String address;
    @NotBlank(message = "Map cannot be blank")
    private String map;
}

