package com.flightapp.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FlightResponseDTOTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        FlightResponseDTO dto = new FlightResponseDTO();

        LocalDateTime dep = LocalDateTime.now();
        LocalDateTime arr = dep.plusHours(2);

        dto.setFlightId(1);
        dto.setFlightNumber("AI101");
        dto.setAirlineName("Air India");
        dto.setAircraftType("A320");
        dto.setDepartureCity("Delhi");
        dto.setArrivalCity("Mumbai");
        dto.setDepartureTime(dep);
        dto.setArrivalTime(arr);
        dto.setAvailableSeats(50);
        dto.setPricePerSeat(new BigDecimal("4500.00"));

        assertEquals(1, dto.getFlightId());
        assertEquals("AI101", dto.getFlightNumber());
        assertEquals("Air India", dto.getAirlineName());
        assertEquals("A320", dto.getAircraftType());
        assertEquals("Delhi", dto.getDepartureCity());
        assertEquals("Mumbai", dto.getArrivalCity());
        assertEquals(dep, dto.getDepartureTime());
        assertEquals(arr, dto.getArrivalTime());
        assertEquals(50, dto.getAvailableSeats());
        assertEquals(new BigDecimal("4500.00"), dto.getPricePerSeat());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime dep = LocalDateTime.now();
        LocalDateTime arr = dep.plusHours(3);

        FlightResponseDTO dto = new FlightResponseDTO(
                2,
                "AI202",
                "Indigo",
                "B737",
                "BLR",
                "DEL",
                dep,
                arr,
                100,
                new BigDecimal("3500.00")
        );

        assertEquals(2, dto.getFlightId());
        assertEquals("AI202", dto.getFlightNumber());
        assertEquals("Indigo", dto.getAirlineName());
        assertEquals("B737", dto.getAircraftType());
        assertEquals("BLR", dto.getDepartureCity());
        assertEquals("DEL", dto.getArrivalCity());
        assertEquals(dep, dto.getDepartureTime());
        assertEquals(arr, dto.getArrivalTime());
        assertEquals(100, dto.getAvailableSeats());
        assertEquals(new BigDecimal("3500.00"), dto.getPricePerSeat());
    }

    @Test
    void testBuilder() {
        LocalDateTime dep = LocalDateTime.now();
        LocalDateTime arr = dep.plusHours(1);

        FlightResponseDTO dto = FlightResponseDTO.builder()
                .flightId(3)
                .flightNumber("AI303")
                .airlineName("SpiceJet")
                .aircraftType("A319")
                .departureCity("HYD")
                .arrivalCity("GOI")
                .departureTime(dep)
                .arrivalTime(arr)
                .availableSeats(10)
                .pricePerSeat(new BigDecimal("2500.00"))
                .build();

        assertEquals(3, dto.getFlightId());
        assertEquals("AI303", dto.getFlightNumber());
        assertEquals("SpiceJet", dto.getAirlineName());
        assertEquals("A319", dto.getAircraftType());
        assertEquals("HYD", dto.getDepartureCity());
        assertEquals("GOI", dto.getArrivalCity());
        assertEquals(dep, dto.getDepartureTime());
        assertEquals(arr, dto.getArrivalTime());
        assertEquals(10, dto.getAvailableSeats());
        assertEquals(new BigDecimal("2500.00"), dto.getPricePerSeat());
    }

    @Test
    void testEqualsHashCodeAndToString() {
        FlightResponseDTO f1 = new FlightResponseDTO(
                1, "AI101", "Air India", "A320",
                "DEL", "BOM", LocalDateTime.now(),
                LocalDateTime.now().plusHours(2), 50,
                new BigDecimal("4500.00")
        );

        FlightResponseDTO f2 = new FlightResponseDTO(
                1, "AI101", "Air India", "A320",
                "DEL", "BOM", f1.getDepartureTime(),
                f1.getArrivalTime(), 50,
                new BigDecimal("4500.00")
        );

        assertEquals(f1, f2);
        assertEquals(f1.hashCode(), f2.hashCode());
        assertNotNull(f1.toString());
    }
}
