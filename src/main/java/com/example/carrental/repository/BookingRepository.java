package com.example.carrental.repository;

import com.example.carrental.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    /**
     * Counts the number of bookings with the given status
     * @param status the status to filter by (e.g., "ACTIVE", "COMPLETED")
     * @return count of bookings with the given status
     */
    long countByStatus(String status);
    
    /**
     * Calculates the total spending across all bookings
     * @return total amount spent across all bookings
     */
    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Booking b")
    double getTotalSpend();
    
    /**
     * Finds all bookings for a specific user
     * @param userId the ID of the user
     * @return list of bookings for the user
     */
    List<Booking> findByUserId(Long userId);
    
    /**
     * Finds all active bookings for a specific user
     * @param userId the ID of the user
     * @return list of active bookings for the user
     */
    List<Booking> findByUserIdAndStatus(Long userId, String status);
}
