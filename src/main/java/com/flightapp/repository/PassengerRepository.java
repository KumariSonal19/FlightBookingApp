package com.flightapp.repository;

import com.flightapp.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger, Integer> {
    
    @Query("SELECT p FROM Passenger p WHERE p.booking.bookingId = :bookingId")
    List<Passenger> findByBookingId(@Param("bookingId") Integer bookingId);
}
