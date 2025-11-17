package com.flightapp.repository;

import com.flightapp.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
    
    Optional<Flight> findByFlightNumber(String flightNumber);
    
    @Query("SELECT f FROM Flight f WHERE f.departureCity = :departure " +
           "AND f.arrivalCity = :arrival " +
           "AND DATE(f.departureTime) = CAST(:date AS DATE) " +
           "AND f.availableSeats > 0 AND f.isActive = true")
    List<Flight> searchFlights(@Param("departure") String departure,
                               @Param("arrival") String arrival,
                               @Param("date") LocalDateTime date);
    
    List<Flight> findByStatus(String status);
}
