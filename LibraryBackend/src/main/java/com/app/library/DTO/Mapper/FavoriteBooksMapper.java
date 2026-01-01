package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.FavoriteBooksResponse;
import com.app.library.Entity.Favoritebooks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FavoriteBooksMapper {
    private final BookMapper bookMapper;
    private final UserMapper userMapper;
    @Autowired
    public FavoriteBooksMapper(BookMapper bookMapper, UserMapper userMapper) {
        this.bookMapper = bookMapper;
        this.userMapper = userMapper;
    }

    public FavoriteBooksResponse toDto(Favoritebooks favoritebooks)
    {
        return new FavoriteBooksResponse(
                favoritebooks.getId(),
                bookMapper.ToDto(favoritebooks.getBook()),
                userMapper.toDto(favoritebooks.getUser()));
    }
}
