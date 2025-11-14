package com.app.library.Controller;

import com.app.library.Service.Interfaces.PromotionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/Promotion")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Promotion Controller", description = "Zarządzanie promocjami w sklepie")
public class PromotionController {
    private final PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionServiceImpl) {
        this.promotionService = promotionServiceImpl;
    }
}
