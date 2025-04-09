package com.app.library.DTO.Response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;

@Getter
public class GenreResponse {

    private final Integer id;
    private final String name;

    public GenreResponse(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
