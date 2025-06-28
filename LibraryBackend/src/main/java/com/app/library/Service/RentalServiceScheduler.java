package com.app.library.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class RentalServiceScheduler {
    final RentalService rentalService;

    @Autowired
    public RentalServiceScheduler(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @Scheduled(cron = "0 0 0 ? * SUN")
    public void autoCheckOverdueRentals() {
        rentalService.checkOverdueRentals();
    }
}
