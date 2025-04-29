package com.app.library.DTO.Response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LibraryResponse {
    private final Integer id;
    private final String location;
    private final String address;
    private final String map;


    public LibraryResponse(Integer id, String location, String address, String map) {
        this.id = id;
        this.location = location;
        this.address = address;
        this.map = map;
    }

}
