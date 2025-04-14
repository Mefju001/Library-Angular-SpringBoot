package com.app.library.Entity;

import lombok.Data;

@Data
public class Penalty {
    Integer days;
    Double price;

    public Double calculatePenaltyPrice(Integer days) {
        if (days <= 0) {
            return 0.0;
        }
        if (days <= 10) {
            return days * 0.50;
        } else if (days <= 20) {
            return 10 + (days - 10) * 0.75;
        } else {
            return 15 + (days - 20) * 1.00;
        }
    }
}
