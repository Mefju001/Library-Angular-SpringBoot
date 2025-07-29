package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.RentalBookResponse;
import com.app.library.Entity.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Mapper(componentModel = "Spring",uses =  {UserMapper.class,BookMapper.class})
public interface RentalBookMapper {

    @Mapping(source = "rentalId", target = "rentalId")
    @Mapping(source = "user", target = "userResponse")
    @Mapping(source = "book", target = "bookResponse")

    @Mapping(target = "rentalStartDate", expression = "java(mapDate(rental.getRentalStartDate()))")
    @Mapping(target = "rentalEndDate", expression = "java(mapDate(rental.getRentalEndDate()))")
    @Mapping(target = "returnRequestDate", expression = "java(mapDate(rental.getReturnRequestDate()))")

    @Mapping(source = "status", target = "status")
    @Mapping(source = "penalty", target = "penalty")

    RentalBookResponse toRentalBookResponse(Rental rental);

    default String mapDate(LocalDate date) {
        return date != null ? date.toString() : null;
    }
}
