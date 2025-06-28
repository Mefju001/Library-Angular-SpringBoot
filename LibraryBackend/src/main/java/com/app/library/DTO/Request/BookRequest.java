package com.app.library.DTO.Request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

public record BookRequest(
        @NotBlank(message = "Title cannot be blank") String title,
        @NotBlank(message = "Author name cannot be blank") String authorName,
        @NotBlank(message = "Author surname cannot be blank") String authorSurname,
        @NotBlank(message = "Author surname cannot be blank") LocalDate publicationDate,
        @Pattern(regexp = "\\d{13}", message = "ISBN must be exactly 13 digits") Long isbn,
        @NotBlank(message = "Genre name cannot be blank")String genreName,
        @NotBlank(message = "Language cannot be blank")String language,
        @NotBlank(message = "Publisher name cannot be blank")String publisherName,
        @Positive(message = "Pages must be a positive number")Integer pages,
        @Positive(message = "Price must be a positive number")Float price){

}
