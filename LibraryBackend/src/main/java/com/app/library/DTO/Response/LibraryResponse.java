package com.app.library.DTO.Response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
public class LibraryResponse {
    private final Integer id;
    private final String name;
    private final String address;

    public LibraryResponse(Integer id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

}
