package com.app.library.DTO.Response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FavoriteBooksResponse {
    private final Integer id;
    private final String title;
    private final String authorName;
    private final String authorSurname;
    private final int publicationYear;
    private final long isbn;
    private final String genreName;
    private final String language;
    private final String publisherName;
    private final int pages;
    private final float price;

    public FavoriteBooksResponse(Integer id, String title, String authorName, String authorSurname, int publicationYear, long isbn, String genreName, String language, String publisherName, int pages, float price) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.authorSurname = authorSurname;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.genreName = genreName;
        this.language = language;
        this.publisherName = publisherName;
        this.pages = pages;
        this.price = price;
    }
}
