package com.app.library.DTO.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


public record LibraryRequest(Integer id,
                             @NotBlank(message = "Library location cannot be blank")String location,
                             @NotBlank(message = "Library address cannot be blank")String address,
                             @NotBlank(message = "Map cannot be blank")String map){
}

