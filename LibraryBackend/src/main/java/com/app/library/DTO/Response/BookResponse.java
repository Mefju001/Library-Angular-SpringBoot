package com.app.library.DTO.Response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
public record BookResponse(Integer id, String title, String authorName, String authorSurname, LocalDate publicationDate,
                           long isbn, String genreName, String language, String publisherName, int pages, float price) {

}