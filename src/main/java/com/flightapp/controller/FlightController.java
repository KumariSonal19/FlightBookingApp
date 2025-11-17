package com.flightapp.controller;

import com.flightapp.dto.FlightResponseDTO;
import com.flightapp.dto.FlightSearchDTO;
import com.flightapp.exception.ValidationException;
import com.flightapp.service.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.flightapp.entity.Flight;


@RestController
@RequestMapping("/v1.0/flight")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class FlightController {
    
    private final FlightService flightService;
 
    @PostMapping("/airline/inventory/add")
    public ResponseEntity<Map<String, Object>> addFlightInventory(@RequestBody Flight flight) {
        log.info("Adding new flight: {}", flight.getFlightNumber());
        
        try {
            FlightResponseDTO addedFlight = flightService.addFlight(flight);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Flight inventory added successfully");
            response.put("flightId", addedFlight.getFlightId());
            response.put("flightNumber", addedFlight.getFlightNumber());
            response.put("airlineName", addedFlight.getAirlineName());
            response.put("departureCity", addedFlight.getDepartureCity());
            response.put("arrivalCity", addedFlight.getArrivalCity());
            response.put("availableSeats", addedFlight.getAvailableSeats());
            response.put("pricePerSeat", addedFlight.getPricePerSeat());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    
    @PostMapping("/search")
    public ResponseEntity<List<FlightResponseDTO>> searchFlights(@RequestBody FlightSearchDTO searchDTO) {
        log.info("Searching flights from {} to {}", searchDTO.getDepartureCity(), searchDTO.getArrivalCity());
        
        if (searchDTO.getDepartureCity() == null || searchDTO.getDepartureCity().trim().isEmpty()) {
            throw new ValidationException("Departure city is required");
        }
        if (searchDTO.getArrivalCity() == null || searchDTO.getArrivalCity().trim().isEmpty()) {
            throw new ValidationException("Arrival city is required");
        }
        if (searchDTO.getDepartureDate() == null) {
            throw new ValidationException("Departure date is required");
        }
        if (searchDTO.getNumberOfPassengers() == null || searchDTO.getNumberOfPassengers() <= 0) {
            throw new ValidationException("Number of passengers must be greater than 0");
        }
        
        List<FlightResponseDTO> flights = flightService.searchFlights(searchDTO);
        log.info("Found {} flights", flights.size());
        return ResponseEntity.ok(flights);
    }
    
    @GetMapping("/{flightId}")
    public ResponseEntity<FlightResponseDTO> getFlightDetails(@PathVariable Integer flightId) {
        log.info("Fetching flight details for ID: {}", flightId);
        
        if (flightId == null || flightId <= 0) {
            throw new ValidationException("Invalid flight ID");
        }
        
        FlightResponseDTO flight = flightService.getFlightById(flightId);
        return ResponseEntity.ok(flight);
    }
}
