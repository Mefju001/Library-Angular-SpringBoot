package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.FavoriteBooksResponse;
import com.app.library.Entity.Favoritebooks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "Spring",uses = {BookMapper.class})
public interface FavoriteBooksMapper {

    FavoriteBooksResponse toDto(Favoritebooks favoritebooks);
}
