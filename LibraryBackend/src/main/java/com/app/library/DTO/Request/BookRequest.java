package com.app.library.DTO.Request;

public class BookRequest {
    private String title;
    private String authorName;
    private String authorSurname;
    private int publicationYear;
    private long isbn;
    private String genreName;
    private String language;
    private String publisherName;
    private int pages;
    private float price;

    public BookRequest(String title, String authorName, String authorSurname, int publicationYear, long isbn, String genreName, String language, String publisherName, int pages, float price) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorSurname() {
        return authorSurname;
    }

    public void setAuthorSurname(String authorSurname) {
        this.authorSurname = authorSurname;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
