package com.flightapp.controller;

import com.flightapp.dto.FlightResponseDTO;
import com.flightapp.dto.FlightSearchDTO;
import com.flightapp.entity.Flight;
import com.flightapp.exception.ResourceNotFoundException;
import com.flightapp.exception.ValidationException;
import com.flightapp.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightControllerTest {
    
    @Mock
    private FlightService flightService;
    
    @InjectMocks
    private FlightController flightController;
    
    private FlightResponseDTO testFlight;
    private FlightSearchDTO searchDTO;
    private Flight testFlightEntity;
    
    @BeforeEach
    public void setUp() {
        testFlight = FlightResponseDTO.builder()
            .flightId(1)
            .flightNumber("AI101")
            .airlineName("Air India")
            .departureCity("Delhi")
            .arrivalCity("Mumbai")
            .departureTime(LocalDateTime.now().plusHours(2))
            .arrivalTime(LocalDateTime.now().plusHours(4))
            .availableSeats(150)
            .pricePerSeat(BigDecimal.valueOf(5000))
            .build();
        
        searchDTO = FlightSearchDTO.builder()
            .departureCity("Delhi")
            .arrivalCity("Mumbai")
            .departureDate(LocalDate.now().plusDays(1))
            .numberOfPassengers(2)
            .tripType("ONE_WAY")
            .build();
        
        testFlightEntity = Flight.builder()
            .flightId(1)
            .flightNumber("AI101")
            .departureCity("Delhi")
            .arrivalCity("Mumbai")
            .totalSeats(180)
            .availableSeats(150)
            .pricePerSeat(BigDecimal.valueOf(5000))
            .build();
    }
    
    @Test
    void testSearchFlights_Success() {
        List<FlightResponseDTO> flights = Arrays.asList(testFlight);
        when(flightService.searchFlights(any(FlightSearchDTO.class)))
            .thenReturn(flights);
        
        ResponseEntity<List<FlightResponseDTO>> response = flightController.searchFlights(searchDTO);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("AI101", response.getBody().get(0).getFlightNumber());
        verify(flightService, times(1)).searchFlights(any());
    }
    
    @Test
    void testSearchFlights_EmptyResult() {
        when(flightService.searchFlights(any(FlightSearchDTO.class)))
            .thenReturn(Collections.emptyList());
        
        ResponseEntity<List<FlightResponseDTO>> response = flightController.searchFlights(searchDTO);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
    }
   
    @Test
    void testSearchFlights_MultipleResults() {
        FlightResponseDTO flight2 = FlightResponseDTO.builder()
            .flightId(2)
            .flightNumber("AI102")
            .airlineName("Air India")
            .departureCity("Delhi")
            .arrivalCity("Mumbai")
            .availableSeats(100)
            .pricePerSeat(BigDecimal.valueOf(4500))
            .build();
        
        when(flightService.searchFlights(any(FlightSearchDTO.class)))
            .thenReturn(Arrays.asList(testFlight, flight2));
        
        ResponseEntity<List<FlightResponseDTO>> response = flightController.searchFlights(searchDTO);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }
   
    @Test
    void testSearchFlights_InvalidInput_NullDepartureCity() {
        searchDTO.setDepartureCity(null);
        
        assertThrows(ValidationException.class, () -> flightController.searchFlights(searchDTO));
    }
    
    @Test
    void testSearchFlights_InvalidInput_NullArrivalCity() {
        searchDTO.setArrivalCity(null);
        
        assertThrows(ValidationException.class, () -> flightController.searchFlights(searchDTO));
    }
    
    @Test
    void testSearchFlights_InvalidInput_NullDate() {
        searchDTO.setDepartureDate(null);
        
        assertThrows(ValidationException.class, () -> flightController.searchFlights(searchDTO));
    }
   
    @Test
    void testSearchFlights_InvalidInput_InvalidPassengers() {
        searchDTO.setNumberOfPassengers(0);
        
        assertThrows(ValidationException.class, () -> flightController.searchFlights(searchDTO));
    }
  
    @Test
    void testGetFlightById_Success() {
        when(flightService.getFlightById(1)).thenReturn(testFlight);
        
        ResponseEntity<FlightResponseDTO> response = flightController.getFlightDetails(1);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("AI101", response.getBody().getFlightNumber());
        assertEquals(150, response.getBody().getAvailableSeats());
    }
    
    @Test
    void testGetFlightById_NotFound() {
        when(flightService.getFlightById(999))
            .thenThrow(new ResourceNotFoundException("Flight not found"));
        
        assertThrows(ResourceNotFoundException.class, () -> flightController.getFlightDetails(999));
    }
    
    @Test
    void testGetFlightById_InvalidId() {
        assertThrows(ValidationException.class, () -> flightController.getFlightDetails(-1));
    }
    
    @Test
    void testGetFlightById_InvalidId_Zero() {
        assertThrows(ValidationException.class, () -> flightController.getFlightDetails(0));
    }
    
    @Test
    void testAddFlightInventory_Success() {
        when(flightService.addFlight(any(Flight.class)))
            .thenReturn(testFlight);
        
        ResponseEntity<Map<String, Object>> response = flightController.addFlightInventory(testFlightEntity);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("message"));
        assertEquals("Flight inventory added successfully", response.getBody().get("message"));
        verify(flightService, times(1)).addFlight(any());
    }
    
    @Test
    void testSearchFlights_DifferentCities() {
        FlightSearchDTO newSearch = FlightSearchDTO.builder()
            .departureCity("Bangalore")
            .arrivalCity("Delhi")
            .departureDate(LocalDate.now().plusDays(2))
            .numberOfPassengers(1)
            .tripType("ONE_WAY")
            .build();
        
        when(flightService.searchFlights(any(FlightSearchDTO.class)))
            .thenReturn(Collections.emptyList());
        
        ResponseEntity<List<FlightResponseDTO>> response = flightController.searchFlights(newSearch);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }
    
    @Test
    void testGetFlightById_LargeId() {
        when(flightService.getFlightById(999999))
            .thenThrow(new ResourceNotFoundException("Flight not found"));
        
        assertThrows(ResourceNotFoundException.class, () -> flightController.getFlightDetails(999999));
    }
}
