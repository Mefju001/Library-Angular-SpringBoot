package com.app.library.DTO.Response;



import java.time.LocalDate;

public record BookResponse(String title, String authorName, String authorSurname, LocalDate publicationDate,
                           long isbn, String genreName, String language, String publisherName, int pages, float price) {

}