package com.app.library.DTO.Mapper;

import com.app.library.DTO.Response.LibraryBookResponse;
import com.app.library.DTO.Response.LoanBookResponse;
import com.app.library.Entity.LibraryBook;
import com.app.library.Entity.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
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
    @Mapping(source = "rental.book.publicationDate", target = "bookPublicationDate")
    @Mapping(source = "book.isbn", target = "bookIsbn")
    @Mapping(source = "book.language", target = "bookLanguage")
    @Mapping(source = "book.pages", target = "bookPages")
    @Mapping(source = "book.price", target = "bookPrice")
    @Mapping(source = "book.oldprice", target = "bookOldPrice")

    @Mapping(source = "rentalStartDate", target = "rentalStartDate")
    @Mapping(source = "rentalEndDate", target = "rentalEndDate")
    @Mapping(source = "returnRequestDate", target = "returnRequestDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "penalty", target = "penalty")
    @Mapping(source = "days", target = "days")
    @Mapping(source = "remainingDays.days", target = "remainingDays")
    @Mapping(source = "overdue", target = "overdue")
        LoanBookResponse toloanBookResponse(Rental rental);
}
