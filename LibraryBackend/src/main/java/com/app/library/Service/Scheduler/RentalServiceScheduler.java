package com.app.library.Service.Scheduler;

import com.app.library.Service.Batch.RentalBatchService;
import com.app.library.Service.Interfaces.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class RentalServiceScheduler {
    final RentalBatchService rentalBatchService;

    @Autowired
    public RentalServiceScheduler(RentalBatchService rentalBatchService) {
        this.rentalBatchService = rentalBatchService;
    }

    @Scheduled(cron = "0 0 0 ? * SUN")
    public void autoCheckOverdueRentals() {
        rentalBatchService.checkOverdueRentals();
    }
}
