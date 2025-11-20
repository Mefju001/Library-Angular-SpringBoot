package com.app.library.Service.Interfaces;

import com.app.library.DTO.Response.GenreResponse;
import com.app.library.Entity.Genre;

import java.util.List;

public interface GenreService {
    List<GenreResponse> findAll();
    Genre getOrCreateGenre(String name);
}
