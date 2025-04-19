package com.app.library.Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Promotions")
public class Promotions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private BigDecimal discountValue;
    private Boolean isActive = true;
    @OneToMany(mappedBy = "promotions", cascade = CascadeType.ALL)
    private List<BookPromotion> bookPromotions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public List<BookPromotion> getBookPromotions() {
        return bookPromotions;
    }

    public void setBookPromotions(List<BookPromotion> bookPromotions) {
        this.bookPromotions = bookPromotions;
    }
}
