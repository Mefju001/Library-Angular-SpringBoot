package com.app.library.DTO.Response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
public record FavoriteBooksResponse(Integer id, String title, String authorName, String authorSurname,
                                    LocalDate publicationDate, Long isbn, String genreName, String language,
                                    String publisherName, Integer pages, Float price) {
}
