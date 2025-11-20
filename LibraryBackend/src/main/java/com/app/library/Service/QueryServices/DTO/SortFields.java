package com.app.library.Service.QueryServices.DTO;

import java.util.Map;

public class SortFields {
    private static final Map<String,String> sortFields = Map.of(
            "title","title",
            "price","price",
            "year","publicationDate"
    );
    public static String findFieldInMap(String field)
    {
        if(field == null||field.isEmpty())
            return null;
        return sortFields.get(field.trim().toLowerCase());
    }
}
