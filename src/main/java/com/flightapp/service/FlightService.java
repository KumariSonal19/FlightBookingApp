package com.flightapp.service;

import com.flightapp.dto.FlightResponseDTO;
import com.flightapp.dto.FlightSearchDTO;
import com.flightapp.entity.Flight;
import com.flightapp.repository.FlightRepository;
import com.flightapp.exception.ResourceNotFoundException;
import com.flightapp.exception.ValidationException;  
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;  
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FlightService {
    
    private final FlightRepository flightRepository;
    
    public List<FlightResponseDTO> searchFlights(FlightSearchDTO searchDTO) {
        log.info("Searching flights from {} to {}", searchDTO.getDepartureCity(), searchDTO.getArrivalCity());
        
        LocalDateTime departureDateTime = searchDTO.getDepartureDate()
            .atStartOfDay(ZoneId.systemDefault()).toLocalDateTime();
        
        List<Flight> flights = flightRepository.searchFlights(
            searchDTO.getDepartureCity(),
            searchDTO.getArrivalCity(),
            departureDateTime
        );
        
        log.info("Found {} flights", flights.size());
        return flights.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional  // ← ADD THIS - Need transactional for write operation
    public FlightResponseDTO addFlight(Flight flight) {
        //Validate flight
        if (flight.getFlightNumber() == null || flight.getFlightNumber().isEmpty()) {
            throw new ValidationException("Flight number is required");
        }
        if (flight.getAirline() == null || flight.getAirline().getAirlineId() == null) {
            throw new ValidationException("Airline ID is required");
        }
        if (flight.getDepartureCity() == null || flight.getArrivalCity() == null) {
            throw new ValidationException("Departure and arrival cities are required");
        }
        if (flight.getTotalSeats() == null || flight.getTotalSeats() <= 0) {
            throw new ValidationException("Total seats must be greater than 0");
        }
        if (flight.getPricePerSeat() == null || flight.getPricePerSeat().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Price per seat must be greater than 0");
        }
        
        //Check if flight already exists
        if (flightRepository.findByFlightNumber(flight.getFlightNumber()).isPresent()) {
            throw new IllegalArgumentException("Flight with this number already exists");
        }
        
        //Set initial values
        flight.setAvailableSeats(flight.getTotalSeats());
        flight.setStatus("ACTIVE");
        flight.setIsActive(true);
        
        //Save and return
        Flight savedFlight = flightRepository.save(flight);
        return convertToDTO(savedFlight);
    }

    
    public FlightResponseDTO getFlightById(Integer flightId) {
        log.info("Fetching flight with ID: {}", flightId);
        
        Flight flight = flightRepository.findById(flightId)
            .orElseThrow(() -> new ResourceNotFoundException("Flight not found with ID: " + flightId));
        
        return convertToDTO(flight);
    }
    
    @Transactional
    public void addFlightInventory(Flight flight) {
        log.info("Adding flight inventory for flight: {}", flight.getFlightNumber());
        
        if (flightRepository.findByFlightNumber(flight.getFlightNumber()).isPresent()) {
            throw new IllegalArgumentException("Flight with this number already exists");
        }
        
        flight.setAvailableSeats(flight.getTotalSeats());
        flightRepository.save(flight);
        log.info("Flight inventory added successfully");
    }
    
    private FlightResponseDTO convertToDTO(Flight flight) {
        return FlightResponseDTO.builder()
            .flightId(flight.getFlightId())
            .flightNumber(flight.getFlightNumber())
            .airlineName(flight.getAirline().getAirlineName())
            .aircraftType(flight.getAircraftType())
            .departureCity(flight.getDepartureCity())
            .arrivalCity(flight.getArrivalCity())
            .departureTime(flight.getDepartureTime())
            .arrivalTime(flight.getArrivalTime())
            .availableSeats(flight.getAvailableSeats())
            .pricePerSeat(flight.getPricePerSeat())
            .build();
    }
}
