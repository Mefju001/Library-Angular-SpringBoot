package com.app.library.Controller;

import com.app.library.Service.Interfaces.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/api/admin/promotion")
@Tag(name = "Admin promotion Controller", description = "Udostępnia funkcję dla administratora w adminPanelu")
public class AdminPromotionController {
    private final PromotionService promotionService;

    public AdminPromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/promotion")
    @Operation(summary = "Ustawia promocję na książce", description = "Związuje promocję z książką na podstawie identyfikatorów książki i promocji.")
    public void setpromontionspriceonbook(@Parameter(description = "ID książki, którą przypisujemy do promocji")
                                          @RequestParam Integer bookId,
                                          @Parameter(description = "ID promocji, którą przypisujemy do książki")
                                          @RequestParam long promotionId) {
        promotionService.setPromotion(bookId, promotionId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/promotion/deactive")
    @Operation(summary = "Dezaktywuje promocję", description = "Dezaktywuje promocję na podstawie identyfikatora promocji.")
    public void deactivepromotions(@Parameter(description = "ID promocji, którą chcemy dezaktywować")
                                   @RequestParam long promotionId) {
        promotionService.deactivatePromotion(promotionId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/promotion/{id}")
    @Operation(summary = "Usuwa promocję", description = "Usuwa promocję na podstawie identyfikatora promocji.")
    public ResponseEntity<?> deletepromotion(@Parameter(description = "ID promocji, którą chcemy usunąć")
                                             @PathVariable Long id) {
        Boolean aBoolean = promotionService.deleteBookPromotion(id);
        if (aBoolean.equals(Boolean.TRUE))
            return ResponseEntity.ok(true);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
