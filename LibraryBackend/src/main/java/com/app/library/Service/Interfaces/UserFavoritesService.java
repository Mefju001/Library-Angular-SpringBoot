package com.app.library.Service.Interfaces;

import com.app.library.DTO.Response.FavoriteBooksResponse;
import com.app.library.Entity.Favoritebooks;
import jakarta.transaction.Transactional;

import java.util.List;

public interface UserFavoritesService {
    List<FavoriteBooksResponse> findAllLikedBooks(Long userId);


    FavoriteBooksResponse addfavoritebooks(Long isbn, Long userId);

    FavoriteBooksResponse updatefavoritebooks(Favoritebooks favoritebooks);

    void deletefavoritebooks(Integer id);
}
