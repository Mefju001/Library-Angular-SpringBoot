package com.app.library.Repository;

import com.app.library.Entity.Rental;
import com.app.library.Entity.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RentalRepository extends JpaRepository<Rental,Integer> {
    List<Rental>findRentalsByUser_Id(Long userId);
    List<Rental>findRentalsByUser_IdAndStatusIs(Long userId, RentalStatus rentalStatus);
    Optional<Rental> findRentalByBook_IdAndUser_Id(Integer BookId, Long userId);
    Long countByStatusIn(Collection<RentalStatus> statuses);
    List<Rental>findByStatusInAndRentalEndDateBefore(Collection<RentalStatus> rentalStatus, LocalDate date);

    List<Rental>findRentalsByStatusIn(Collection<RentalStatus> rentalStatuses);
    @Query("SELECT f.book.id FROM Rental f WHERE f.user.id = :userId")
    Set<Long> findBook_IdByUser_Id(Long userId);
}
