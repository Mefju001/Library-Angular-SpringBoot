package com.app.library.DTO.Request;

import jakarta.validation.constraints.NotBlank;


public record LibraryRequest(
        @NotBlank(message = "Library location cannot be blank") String location,
        @NotBlank(message = "Library address cannot be blank") String address,
        @NotBlank(message = "Map cannot be blank") String map) {
}

