package com.flightapp.repository;

import com.flightapp.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Integer> {
    
    @Query("SELECT s FROM Seat s WHERE s.flight.flightId = :flightId AND s.isAvailable = true")
    List<Seat> findAvailableSeatsByFlight(@Param("flightId") Integer flightId);
    
    @Query("SELECT s FROM Seat s WHERE s.flight.flightId = :flightId AND s.seatNumber = :seatNumber")
    Optional<Seat> findByFlightIdAndSeatNumber(@Param("flightId") Integer flightId, @Param("seatNumber") String seatNumber);
}
