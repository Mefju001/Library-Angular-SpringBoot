package com.app.library.Repository;

import com.app.library.Entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental,Integer> {
    List<Rental>findRentalsByUser_Id(Long userId);
}
