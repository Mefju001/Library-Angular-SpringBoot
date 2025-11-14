package com.app.library.DTO.Request;

import java.time.LocalDate;

public record BookCriteria (
        String Title,
        String genre_name,
        String publisher_name,
        String authorName,
        String authorSurname,
        Float minPrice,
        Float maxPrice,
        LocalDate startYear,
        LocalDate endYear,

        String sortByField,
        String direction,
        int page,
        int size){ }
