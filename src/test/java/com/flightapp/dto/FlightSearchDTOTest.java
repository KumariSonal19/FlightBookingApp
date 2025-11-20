package com.flightapp.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FlightSearchDTOTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        FlightSearchDTO dto = new FlightSearchDTO();

        LocalDate depDate = LocalDate.now();
        LocalDate retDate = depDate.plusDays(5);

        dto.setDepartureCity("Delhi");
        dto.setArrivalCity("Mumbai");
        dto.setDepartureDate(depDate);
        dto.setReturnDate(retDate);
        dto.setTripType("ROUND_TRIP");
        dto.setNumberOfPassengers(2);

        assertEquals("Delhi", dto.getDepartureCity());
        assertEquals("Mumbai", dto.getArrivalCity());
        assertEquals(depDate, dto.getDepartureDate());
        assertEquals(retDate, dto.getReturnDate());
        assertEquals("ROUND_TRIP", dto.getTripType());
        assertEquals(2, dto.getNumberOfPassengers());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDate depDate = LocalDate.now();
        LocalDate retDate = depDate.plusDays(3);

        FlightSearchDTO dto = new FlightSearchDTO(
                "BLR",
                "DEL",
                depDate,
                retDate,
                "ONE_WAY",
                1
        );

        assertEquals("BLR", dto.getDepartureCity());
        assertEquals("DEL", dto.getArrivalCity());
        assertEquals(depDate, dto.getDepartureDate());
        assertEquals(retDate, dto.getReturnDate());
        assertEquals("ONE_WAY", dto.getTripType());
        assertEquals(1, dto.getNumberOfPassengers());
    }

    @Test
    void testBuilder() {
        LocalDate depDate = LocalDate.now();

        FlightSearchDTO dto = FlightSearchDTO.builder()
                .departureCity("HYD")
                .arrivalCity("GOI")
                .departureDate(depDate)
                .returnDate(null)
                .tripType("ONE_WAY")
                .numberOfPassengers(4)
                .build();

        assertEquals("HYD", dto.getDepartureCity());
        assertEquals("GOI", dto.getArrivalCity());
        assertEquals(depDate, dto.getDepartureDate());
        assertNull(dto.getReturnDate());
        assertEquals("ONE_WAY", dto.getTripType());
        assertEquals(4, dto.getNumberOfPassengers());
    }

    @Test
    void testEqualsHashCodeAndToString() {
        LocalDate depDate = LocalDate.now();

        FlightSearchDTO s1 = new FlightSearchDTO(
                "A", "B", depDate, null, "ONE_WAY", 1
        );
        FlightSearchDTO s2 = new FlightSearchDTO(
                "A", "B", depDate, null, "ONE_WAY", 1
        );

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotNull(s1.toString());
    }
}
