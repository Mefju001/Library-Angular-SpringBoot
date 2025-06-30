package com.app.library.DTO.Request;

import java.time.LocalDate;

public record BookSearchCriteria (
        String Title,
        String genre_name,
        String publisher_name,
        String authorName,
        String authorSurname,
        Float minPrice,
        Float maxPrice,
        LocalDate startYear,
        LocalDate endYear,
        int page,
        int size){ }
