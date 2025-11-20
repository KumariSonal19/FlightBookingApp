package com.flightapp.dto;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingRequestDTOTest {

    @Test
    void testNoArgsConstructor() {
        BookingRequestDTO dto = new BookingRequestDTO();
        assertNotNull(dto);
    }

    @Test
    void testAllArgsConstructor() {
        PassengerDTO passenger = new PassengerDTO("John Doe", "Male", 30, "Vegetarian");
        List<PassengerDTO> passengers = Arrays.asList(passenger);
        
        BookingRequestDTO dto = new BookingRequestDTO(
            "test@email.com",
            1,
            passengers,
            "ONE_WAY",
            null
        );
        
        assertEquals("test@email.com", dto.getUserEmail());
        assertEquals(1, dto.getNumberOfPassengers());
        assertEquals(passengers, dto.getPassengers());
        assertEquals("ONE_WAY", dto.getTripType());
        assertNull(dto.getReturnFlightId());
    }

    @Test
    void testBuilder() {
        PassengerDTO passenger = new PassengerDTO("Jane Doe", "Female", 25, "Non-Vegetarian");
        List<PassengerDTO> passengers = Arrays.asList(passenger);
        
        BookingRequestDTO dto = BookingRequestDTO.builder()
            .userEmail("jane@email.com")
            .numberOfPassengers(1)
            .passengers(passengers)
            .tripType("ROUND_TRIP")
            .returnFlightId(101)
            .build();
        
        assertEquals("jane@email.com", dto.getUserEmail());
        assertEquals(1, dto.getNumberOfPassengers());
        assertEquals(passengers, dto.getPassengers());
        assertEquals("ROUND_TRIP", dto.getTripType());
        assertEquals(101, dto.getReturnFlightId());
    }

    @Test
    void testGettersAndSetters() {
        BookingRequestDTO dto = new BookingRequestDTO();
        PassengerDTO passenger = new PassengerDTO("Test User", "Male", 40, "Vegan");
        List<PassengerDTO> passengers = Arrays.asList(passenger);
        
        dto.setUserEmail("user@test.com");
        dto.setNumberOfPassengers(2);
        dto.setPassengers(passengers);
        dto.setTripType("ONE_WAY");
        dto.setReturnFlightId(200);
        
        assertEquals("user@test.com", dto.getUserEmail());
        assertEquals(2, dto.getNumberOfPassengers());
        assertEquals(passengers, dto.getPassengers());
        assertEquals("ONE_WAY", dto.getTripType());
        assertEquals(200, dto.getReturnFlightId());
    }

    @Test
    void testEqualsAndHashCode() {
        PassengerDTO passenger = new PassengerDTO("John Doe", "Male", 30, "Vegetarian");
        List<PassengerDTO> passengers = Arrays.asList(passenger);
        
        BookingRequestDTO dto1 = BookingRequestDTO.builder()
            .userEmail("test@email.com")
            .numberOfPassengers(1)
            .passengers(passengers)
            .tripType("ONE_WAY")
            .returnFlightId(null)
            .build();
        
        BookingRequestDTO dto2 = BookingRequestDTO.builder()
            .userEmail("test@email.com")
            .numberOfPassengers(1)
            .passengers(passengers)
            .tripType("ONE_WAY")
            .returnFlightId(null)
            .build();
        
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BookingRequestDTO dto = BookingRequestDTO.builder()
            .userEmail("test@email.com")
            .numberOfPassengers(1)
            .tripType("ONE_WAY")
            .build();
        
        String toString = dto.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("test@email.com"));
        assertTrue(toString.contains("ONE_WAY"));
    }
}