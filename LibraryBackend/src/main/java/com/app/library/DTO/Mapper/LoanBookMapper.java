package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.LoanBookResponse;
import com.app.library.Entity.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Mapper(componentModel = "Spring")
public interface LoanBookMapper {

    @Mapping(source = "rentalId", target = "rentalId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(expression = "java(rental.getUser().getName() + \" \" + rental.getUser().getSurname())", target = "userFullName")
    @Mapping(source = "user.email", target = "userEmail")

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    @Mapping(source = "book.author.name", target = "bookAuthorName")
    @Mapping(source = "book.author.surname", target = "bookAuthorSurname")
    @Mapping(source = "book.genre.name", target = "bookGenre")
    @Mapping(source = "book.publisher.name", target = "bookPublisher")
    @Mapping(source = "book.publicationDate", target = "bookPublicationDate")
    @Mapping(source = "book.isbn", target = "bookIsbn")
    @Mapping(source = "book.language", target = "bookLanguage")
    @Mapping(source = "book.pages", target = "bookPages")
    @Mapping(source = "book.price", target = "bookPrice")
    @Mapping(source = "book.oldprice", target = "bookOldPrice")

    @Mapping(target = "rentalStartDate", expression = "java(mapDate(rental.getRentalStartDate()))")
    @Mapping(target = "rentalEndDate", expression = "java(mapDate(rental.getRentalEndDate()))")
    @Mapping(target = "returnRequestDate", expression = "java(mapDate(rental.getReturnRequestDate()))")

    @Mapping(source = "status", target = "status")
    @Mapping(source = "penalty", target = "penalty")

    @Mapping(target = "days", expression = "java(rental.getDays())")
    @Mapping(target = "remainingDays", expression = "java(rental.getRemainingDays().getDays())")
    @Mapping(target = "overdue", expression = "java(rental.getRemainingDays().isOverdue())")
    LoanBookResponse toloanBookResponse(Rental rental);

    default String mapDate(LocalDate date) {
        return date != null ? date.toString() : null;
    }
}
