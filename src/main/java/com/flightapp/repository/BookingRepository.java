package com.flightapp.repository;

import com.flightapp.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    
    Optional<Booking> findByPnrNumber(String pnrNumber);
    
    @Query("SELECT b FROM Booking b WHERE b.user.email = :email ORDER BY b.bookingDate DESC")
    List<Booking> findByUserEmail(@Param("email") String email);
    
    @Query("SELECT b FROM Booking b WHERE b.user.userId = :userId ORDER BY b.bookingDate DESC")
    List<Booking> findByUserId(@Param("userId") Integer userId);
    
    List<Booking> findByBookingStatus(String status);
}
