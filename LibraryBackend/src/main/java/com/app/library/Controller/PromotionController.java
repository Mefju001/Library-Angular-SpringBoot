package com.app.library.Controller;

import com.app.library.Service.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Promotion")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Promotion Controller", description = "Zarządzanie promocjami w sklepie")
public class PromotionController {
    private final PromotionService promotionService;
    @Autowired
    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }
    @GetMapping("/promotion")
    @Operation(summary = "Ustawia promocję na książce", description = "Związuje promocję z książką na podstawie identyfikatorów książki i promocji.")
    public void setpromontionspriceonbook(@Parameter(description = "ID książki, którą przypisujemy do promocji")
                                              @RequestParam Integer bookId,
                                          @Parameter(description = "ID promocji, którą przypisujemy do książki")
                                          @RequestParam long promotionId) {
        promotionService.setpromotion(bookId,promotionId);
    }
    @GetMapping("/promotion/deactive")
    @Operation(summary = "Dezaktywuje promocję", description = "Dezaktywuje promocję na podstawie identyfikatora promocji.")
    public void deactivepromotions(@Parameter(description = "ID promocji, którą chcemy dezaktywować")
                                       @RequestParam long promotionId)
    {
        promotionService.deactivatePromotion(promotionId);
    }
    @DeleteMapping("/promotion/{id}")
    @Operation(summary = "Usuwa promocję", description = "Usuwa promocję na podstawie identyfikatora promocji.")
    public ResponseEntity<?> deletepromotion(@Parameter(description = "ID promocji, którą chcemy usunąć")
                                                 @PathVariable Long id) {
        Boolean aBoolean = promotionService.deleteBookPromotion(id);
        if(aBoolean.equals(Boolean.TRUE))
            return ResponseEntity.ok(true);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
