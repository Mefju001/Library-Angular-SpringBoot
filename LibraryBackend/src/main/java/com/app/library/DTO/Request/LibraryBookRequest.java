package com.app.library.DTO.Request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
@Getter
@Setter
public class LibraryBookRequest {
    private Integer id;
    private String title;
    private String authorName;
    private String authorSurname;
    private LocalDate publicationDate;
    private long isbn;
    private String genreName;
    private String language;
    private String publisherName;
    private int pages;
    private float price;
    //Library
    private Integer idLibrary;
    private String name;
    private String address;
}
