package com.app.library.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class LibraryBookRequest {
    private Integer id;
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
    @NotBlank(message = "Genre cannot be blank")
    private String genreName;
    @NotBlank(message = "Language cannot be blank")
    private String language;
    @NotBlank(message = "Publisher name cannot be blank")
    private String publisherName;
    @Positive(message = "Number of pages must be positive")
    private int pages;
    @Positive(message = "Price must be positive")
    private float price;
    //Library
    @NotNull(message = "Library ID must be provided")
    private Integer idLibrary;
    @NotBlank(message = "Library name cannot be blank")
    private String name;
    @NotBlank(message = "Library address cannot be blank")
    private String address;

}
