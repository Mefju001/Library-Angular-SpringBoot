package com.app.library.DTO.Response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * @param id        Book
 * @param idLibrary Library
 */
@Builder
public record LibraryBookResponse(Integer id, String title, String authorName, String authorSurname,
                                  LocalDate publicationDate, long isbn, String genreName, String language,
                                  String publisherName, int pages, float price, Integer idLibrary, String location,
                                  String address, String map) {
}
