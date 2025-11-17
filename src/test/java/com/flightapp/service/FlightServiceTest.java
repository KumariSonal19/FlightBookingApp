package com.flightapp.service;

import com.flightapp.dto.FlightSearchDTO;
import com.flightapp.entity.Airline;
import com.flightapp.entity.Flight;
import com.flightapp.exception.ResourceNotFoundException;
import com.flightapp.exception.ValidationException;
import com.flightapp.repository.FlightRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    private Flight testFlight;
    private Airline testAirline;
    private FlightSearchDTO searchDTO;

    @BeforeEach
    void setUp() {
        testAirline = Airline.builder()
                .airlineId(1)
                .airlineName("Air India")
                .airlineCode("AI")
                .build();

        testFlight = Flight.builder()
                .flightId(1)
                .flightNumber("AI101")
                .airline(testAirline)
                .departureCity("Delhi")
                .arrivalCity("Mumbai")
                .departureTime(LocalDateTime.now().plusHours(2))
                .arrivalTime(LocalDateTime.now().plusHours(4))
                .totalSeats(180)
                .availableSeats(150)
                .pricePerSeat(BigDecimal.valueOf(5000))
                .aircraftType("Boeing 737")
                .status("ACTIVE")
                .isActive(true)
                .build();

        searchDTO = FlightSearchDTO.builder()
                .departureCity("Delhi")
                .arrivalCity("Mumbai")
                .departureDate(LocalDate.now().plusDays(1))
                .numberOfPassengers(2)
                .tripType("ONE_WAY")
                .build();
    }

    @Test
    void testSearchFlights_Success() {
        when(flightRepository.searchFlights(anyString(), anyString(), any(LocalDateTime.class)))
                .thenReturn(List.of(testFlight));

        var result = flightService.searchFlights(searchDTO);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("AI101", result.get(0).getFlightNumber());
        verify(flightRepository, times(1)).searchFlights(anyString(), anyString(), any());
    }

    @Test
    void testSearchFlights_EmptyResult() {
        when(flightRepository.searchFlights(anyString(), anyString(), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        var result = flightService.searchFlights(searchDTO);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchFlights_NoResults() {
        when(flightRepository.searchFlights(any(), any(), any()))
                .thenReturn(Collections.emptyList());

        var result = flightService.searchFlights(searchDTO);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetFlightById_Success() {
        when(flightRepository.findById(1)).thenReturn(Optional.of(testFlight));

        var result = flightService.getFlightById(1);

        assertNotNull(result);
        assertEquals("AI101", result.getFlightNumber());
        assertEquals(150, result.getAvailableSeats());
    }

    @Test
    void testGetFlightById_NotFound() {
        when(flightRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> flightService.getFlightById(1));
    }

    @Test
    void testGetFlightById_InvalidId() {
        when(flightRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> flightService.getFlightById(999));
    }

    @Test
    void testAddFlightInventory_Success() {
        when(flightRepository.findByFlightNumber("AI101")).thenReturn(Optional.empty());
        when(flightRepository.save(any(Flight.class))).thenReturn(testFlight);

        flightService.addFlightInventory(testFlight);

        verify(flightRepository, times(1)).save(testFlight);
    }

    @Test
    void testAddFlightInventory_Duplicate() {
        when(flightRepository.findByFlightNumber("AI101")).thenReturn(Optional.of(testFlight));

        assertThrows(IllegalArgumentException.class,
                () -> flightService.addFlightInventory(testFlight));
    }

    @Test
    void testAddFlight_DuplicateFlightNumber() {
        when(flightRepository.findByFlightNumber("AI101"))
                .thenReturn(Optional.of(testFlight));

        assertThrows(IllegalArgumentException.class,
                () -> flightService.addFlightInventory(testFlight));
    }

    @Test
    void testAddFlight_ValidationError() {
        Flight invalidFlight = Flight.builder().build();

        assertThrows(ValidationException.class,
                () -> flightService.addFlight(invalidFlight));
    }

    @Test
    void testAddFlight_NullFlightNumber() {
        Flight invalidFlight = Flight.builder()
                .flightNumber(null)
                .airline(testAirline)
                .build();

        assertThrows(ValidationException.class,
                () -> flightService.addFlight(invalidFlight));
    }

    @Test
    void testAddFlight_NullAirline() {
        Flight invalidFlight = Flight.builder()
                .flightNumber("AI101")
                .airline(null)
                .build();

        assertThrows(ValidationException.class,
                () -> flightService.addFlight(invalidFlight));
    }

    @Test
    void testAddFlight_NullCities() {
        Flight invalidFlight = Flight.builder()
                .flightNumber("AI101")
                .airline(testAirline)
                .departureCity(null)
                .arrivalCity("Mumbai")
                .build();

        assertThrows(ValidationException.class,
                () -> flightService.addFlight(invalidFlight));
    }

    @Test
    void testAddFlight_InvalidSeats() {
        Flight invalidFlight = Flight.builder()
                .flightNumber("AI101")
                .airline(testAirline)
                .departureCity("Delhi")
                .arrivalCity("Mumbai")
                .totalSeats(0)
                .build();

        assertThrows(ValidationException.class,
                () -> flightService.addFlight(invalidFlight));
    }

    @Test
    void testAddFlight_InvalidPrice() {
        Flight invalidFlight = Flight.builder()
                .flightNumber("AI101")
                .airline(testAirline)
                .departureCity("Delhi")
                .arrivalCity("Mumbai")
                .totalSeats(100)
                .pricePerSeat(BigDecimal.ZERO)
                .build();

        assertThrows(ValidationException.class,
                () -> flightService.addFlight(invalidFlight));
    }

    @Test
    void testAddFlight_Success() {
        when(flightRepository.findByFlightNumber("AI101")).thenReturn(Optional.empty());
        when(flightRepository.save(any(Flight.class))).thenReturn(testFlight);

        var result = flightService.addFlight(testFlight);

        assertNotNull(result);
        assertEquals("AI101", result.getFlightNumber());
        verify(flightRepository, times(1)).save(any(Flight.class));
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

        when(flightRepository.searchFlights(anyString(), anyString(), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        var result = flightService.searchFlights(newSearch);

        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchFlights_MultipleResults() {
        Flight flight2 = Flight.builder()
                .flightId(2)
                .flightNumber("AI102")
                .airline(testAirline)
                .departureCity("Delhi")
                .arrivalCity("Mumbai")
                .departureTime(LocalDateTime.now().plusHours(5))
                .arrivalTime(LocalDateTime.now().plusHours(7))
                .build();

        when(flightRepository.searchFlights(anyString(), anyString(), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(testFlight, flight2));

        var result = flightService.searchFlights(searchDTO);

        assertEquals(2, result.size());
    }
}
