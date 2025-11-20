package com.app.library.Service.Batch;

import com.app.library.Entity.Rental;
import com.app.library.Entity.RentalStatus;
import com.app.library.Repository.RentalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class RentalBatchService {
    private final RentalRepository rentalRepository;
    @Autowired
    public RentalBatchService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Transactional
    public void approveAll() {
        List<Rental> rentalList = rentalRepository.findRentalsByStatusIn(List.of(RentalStatus.return_requested, RentalStatus.pending, RentalStatus.extend_requested));
        for (Rental rentals : rentalList) {
            if (rentals.getStatus().equals(RentalStatus.return_requested)) {
                rentals.setStatus(RentalStatus.returned);
            } else if (rentals.getStatus().equals(RentalStatus.pending)) {
                rentals.setStatus(RentalStatus.loaned);
            } else if (rentals.getStatus().equals(RentalStatus.extend_requested)) {
                rentals.setStatus(RentalStatus.loaned);
                rentals.setRentalEndDate(rentals.getRentalEndDate().plusMonths(1));
            }
        }
        rentalRepository.saveAll(rentalList);
    }
    @Transactional
    public Map<String, Integer> checkOverdueRentals() {
        List<Rental> rentalList = rentalRepository.findByStatusInAndRentalEndDateBefore(List.of(RentalStatus.loaned), LocalDate.now());
        int updatedcount = 0;
        for (Rental listRental : rentalList) {
            listRental.setStatus(RentalStatus.overdue);
            updatedcount++;
        }
        rentalRepository.saveAll(rentalList);
        Map<String, Integer> response = new HashMap<>();
        response.put("updated", updatedcount);
        return response;
    }
}
