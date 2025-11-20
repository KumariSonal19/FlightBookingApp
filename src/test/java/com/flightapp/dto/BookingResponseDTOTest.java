package com.flightapp.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingResponseDTOTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        BookingResponseDTO dto = new BookingResponseDTO();

        LocalDateTime dep = LocalDateTime.now();
        LocalDateTime bookingDate = dep.minusHours(2);

        dto.setBookingId(10);
        dto.setPnrNumber("PNR12345");
        dto.setFlightNumber("AI101");
        dto.setDepartureCity("Delhi");
        dto.setArrivalCity("Mumbai");
        dto.setDepartureTime(dep);
        dto.setTotalPrice(new BigDecimal("4999.50"));
        dto.setBookingStatus("CONFIRMED");
        dto.setBookingDate(bookingDate);
        dto.setNumberOfPassengers(3);

        assertEquals(10, dto.getBookingId());
        assertEquals("PNR12345", dto.getPnrNumber());
        assertEquals("AI101", dto.getFlightNumber());
        assertEquals("Delhi", dto.getDepartureCity());
        assertEquals("Mumbai", dto.getArrivalCity());
        assertEquals(dep, dto.getDepartureTime());
        assertEquals(new BigDecimal("4999.50"), dto.getTotalPrice());
        assertEquals("CONFIRMED", dto.getBookingStatus());
        assertEquals(bookingDate, dto.getBookingDate());
        assertEquals(3, dto.getNumberOfPassengers());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime dep = LocalDateTime.now();
        LocalDateTime bookingDate = dep.minusHours(1);

        BookingResponseDTO dto = new BookingResponseDTO(
                20,
                "PNR999",
                "AI202",
                "BLR",
                "DEL",
                dep,
                new BigDecimal("2999.00"),
                "CANCELLED",
                bookingDate,
                1
        );

        assertEquals(20, dto.getBookingId());
        assertEquals("PNR999", dto.getPnrNumber());
        assertEquals("AI202", dto.getFlightNumber());
        assertEquals("BLR", dto.getDepartureCity());
        assertEquals("DEL", dto.getArrivalCity());
        assertEquals(dep, dto.getDepartureTime());
        assertEquals(new BigDecimal("2999.00"), dto.getTotalPrice());
        assertEquals("CANCELLED", dto.getBookingStatus());
        assertEquals(bookingDate, dto.getBookingDate());
        assertEquals(1, dto.getNumberOfPassengers());
    }

    @Test
    void testBuilder() {
        LocalDateTime dep = LocalDateTime.now();

        BookingResponseDTO dto = BookingResponseDTO.builder()
                .bookingId(5)
                .pnrNumber("PNR-BUILDER")
                .flightNumber("AI303")
                .departureCity("Hyd")
                .arrivalCity("Goa")
                .departureTime(dep)
                .totalPrice(new BigDecimal("1500.00"))
                .bookingStatus("PENDING")
                .bookingDate(dep.minusDays(1))
                .numberOfPassengers(2)
                .build();

        assertEquals(5, dto.getBookingId());
        assertEquals("PNR-BUILDER", dto.getPnrNumber());
        assertEquals("AI303", dto.getFlightNumber());
        assertEquals("Hyd", dto.getDepartureCity());
        assertEquals("Goa", dto.getArrivalCity());
        assertEquals(dep, dto.getDepartureTime());
        assertEquals(new BigDecimal("1500.00"), dto.getTotalPrice());
        assertEquals("PENDING", dto.getBookingStatus());
        assertEquals(dep.minusDays(1), dto.getBookingDate());
        assertEquals(2, dto.getNumberOfPassengers());
    }

    @Test
    void testEqualsHashCodeAndToString() {
        BookingResponseDTO d1 = new BookingResponseDTO();
        d1.setBookingId(1);
        d1.setPnrNumber("PNR1");

        BookingResponseDTO d2 = new BookingResponseDTO();
        d2.setBookingId(1);
        d2.setPnrNumber("PNR1");

        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
        assertNotNull(d1.toString());
    }
}
