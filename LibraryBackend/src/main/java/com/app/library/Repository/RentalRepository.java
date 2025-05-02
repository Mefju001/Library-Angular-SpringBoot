package com.app.library.Repository;

import com.app.library.Entity.Rental;
import com.app.library.Entity.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<Rental,Integer> {
    List<Rental>findRentalsByUser_IdAndStatusNot(Long userId,RentalStatus rentalStatus);
    List<Rental>findRentalsByUser_IdAndStatusIs(Long userId, RentalStatus rentalStatus);
    Optional<Rental> findRentalByBook_IdAndUser_Id(Integer BookId, Long userId);

}
