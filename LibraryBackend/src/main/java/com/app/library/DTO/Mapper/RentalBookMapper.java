package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.RentalBookResponse;
import com.app.library.Entity.Rental;
import org.springframework.stereotype.Component;

@Component
public class RentalBookMapper {
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public RentalBookMapper(UserMapper userMapper, BookMapper bookMapper) {
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    public RentalBookResponse toDto(Rental rental)
    {
        return new RentalBookResponse(
                rental.getRentalId(),
                userMapper.toDto(rental.getUser()),
                bookMapper.ToDto(rental.getBook()),
                rental.getRentalStartDate().toString(),
                rental.getRentalEndDate().toString(),
                rental.getReturnRequestDate().toString(),
                rental.getStatus().toString(),
                rental.getPenalty(),
                rental.getExtensionCount());
    }

}
