package com.app.library.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public record LibraryBookRequest(Integer id,
                                 @NotBlank(message = "Title cannot be blank")String title,
                                 @NotBlank(message = "Author name cannot be blank")String authorName,
                                 @NotBlank(message = "Author surname cannot be blank")String authorSurname,
                                 @NotNull(message = "Publication date is required")LocalDate publicationDate,
                                 @Pattern(regexp = "\\d{13}", message = "ISBN must be exactly 13 digits")Long isbn,
                                 @NotBlank(message = "Genre cannot be blank")String genreName,
                                 @NotBlank(message = "Language cannot be blank")String language,
                                 @NotBlank(message = "Publisher name cannot be blank")String publisherName,
                                 @Positive(message = "Number of pages must be positive")Integer pages,
                                 @Positive(message = "Price must be positive")Float price,
                                 @NotNull(message = "Library ID must be provided")Integer idLibrary,
                                 @NotBlank(message = "Library name cannot be blank")String name,
                                 @NotBlank(message = "Library address cannot be blank")String address
                                 ) {
}
