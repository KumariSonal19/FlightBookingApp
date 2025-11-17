package com.flightapp.repository;

import com.flightapp.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AirlineRepository extends JpaRepository<Airline, Integer> {
    Optional<Airline> findByAirlineCode(String airlineCode);
    Optional<Airline> findByAirlineName(String airlineName);
}
