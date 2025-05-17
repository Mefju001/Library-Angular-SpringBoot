package com.app.library.DTO.Request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
@Getter
@Setter
public record BookRequest {
    @NotBlank(message = "Title cannot be blank")
    private String title;
    @NotBlank(message = "Author name cannot be blank")
    private String authorName;
    @NotBlank(message = "Author surname cannot be blank")
    private String authorSurname;
    @NotNull(message = "Publication date is required")
    private LocalDate publicationDate;
    @Pattern(regexp = "\\d{13}", message = "ISBN must be exactly 13 digits")
    private long isbn;
    @NotBlank(message = "Genre name cannot be blank")
    private String genreName;
    @NotBlank(message = "Language cannot be blank")
    private String language;
    @NotBlank(message = "Publisher name cannot be blank")
    private String publisherName;
    @Positive(message = "Pages must be a positive number")
    private int pages;
    @Positive(message = "Price must be a positive number")
    private float price;
}
