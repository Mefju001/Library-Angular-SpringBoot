package com.app.library.DTO.Response;

public record RentalBookResponse(
        UserResponse userResponse,
        BookResponse bookResponse,
        String rentalStartDate,
        String rentalEndDate,
        String returnRequestDate,
        String status,
        Double penalty,
        Integer extensionCount
        ) {
}
