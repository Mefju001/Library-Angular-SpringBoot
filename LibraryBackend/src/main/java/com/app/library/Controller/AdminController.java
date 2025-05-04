package com.app.library.Controller;

import com.app.library.DTO.Response.BookResponse;
import com.app.library.Service.BookService;
import com.app.library.Service.RentalService;
import com.app.library.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/adminPanel")
@Tag(name = "Admin Controller", description = "Udostępnia funckję dla administratora w adminPanelu")
public class AdminController {
    private final BookService bookService;
    private final UserService userService;
    private final RentalService rentalService;
    @Autowired
    public AdminController(BookService bookService, UserService userService, RentalService rentalService) {
        this.bookService = bookService;
        this.userService = userService;
        this.rentalService = rentalService;
    }
    @GetMapping("/user/count")
    @Operation(summary = "", description = "")
    public ResponseEntity<Long> getUserCount()
    {
        Long size = userService.getUserCount();
        System.out.println(size);
        return ResponseEntity.ok(size);
    }
    @GetMapping("/loan/count")
    @Operation(summary = "", description = "")
    public ResponseEntity<Long> getActiveBorrowsCount()
    {
        Long size = rentalService.getActiveBorrowsCount();
        return ResponseEntity.ok(size);
    }
}
