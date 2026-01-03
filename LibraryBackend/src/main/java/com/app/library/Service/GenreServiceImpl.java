package com.app.library.Service;

import com.app.library.DTO.Mapper.GenreMapper;
import com.app.library.DTO.Response.GenreResponse;
import com.app.library.Entity.Genre;
import com.app.library.Repository.GenreRepository;
import com.app.library.Service.Interfaces.GenreService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public GenreServiceImpl(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }
    @Override
    public Genre getOrCreateGenre(String name) {
        return genreRepository.findGenreByName(name)
                .orElseGet(() -> genreRepository.save(new Genre(name)));
    }
    @Override
    public List<GenreResponse> findAll() {
        List<Genre> genres = genreRepository.findAll();
        return genres.stream()
                .map(genreMapper::toDto)
                .toList();
    }
}
