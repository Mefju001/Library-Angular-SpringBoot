package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.RentalBookResponse;
import com.app.library.Entity.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RentalBookMapper {

    RentalBookResponse toRentalBookResponse(Rental rental)
    {
        return new RentalBookResponse(
                rental,

        );
    }

}
