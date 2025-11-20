package com.flightapp.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Test
    void testNoArgsConstructorAndDefaults() {
        Booking booking = new Booking();  // triggers field defaults

        // defaults from field initialisers
        assertEquals("CONFIRMED", booking.getBookingStatus());
        assertEquals("ONE_WAY", booking.getTripType());
        assertTrue(booking.getIsActive());   // default isActive = true

        // setters / getters
        booking.setBookingId(1);
        booking.setPnrNumber("PNR123");
        booking.setNumberOfPassengers(2);
        booking.setTotalPrice(new BigDecimal("123.45"));
        booking.setJourneyDate(LocalDate.of(2024, 1, 1));
        booking.setReturnDate(LocalDate.of(2024, 1, 10));

        assertEquals(1, booking.getBookingId());
        assertEquals("PNR123", booking.getPnrNumber());
        assertEquals(2, booking.getNumberOfPassengers());
        assertEquals(new BigDecimal("123.45"), booking.getTotalPrice());
        assertEquals(LocalDate.of(2024, 1, 1), booking.getJourneyDate());
        assertEquals(LocalDate.of(2024, 1, 10), booking.getReturnDate());
    }

    @Test
    void testBuilderCreatesExpectedObject() {
        LocalDateTime now = LocalDateTime.now();

        Booking booking = Booking.builder()
                .bookingId(5)
                .pnrNumber("PNR999")
                .numberOfPassengers(3)
                .totalPrice(new BigDecimal("500.00"))
                .bookingStatus("CANCELLED")
                .bookingDate(now)
                .journeyDate(LocalDate.of(2024, 2, 2))
                .returnDate(LocalDate.of(2024, 2, 5))
                .tripType("ROUND_TRIP")
                .cancellationDate(now.plusDays(1))
                .cancellationReason("Customer request")
                .refundAmount(new BigDecimal("400.00"))
                .isActive(false)
                .createdAt(now.minusDays(1))
                .updatedAt(now)
                .passengers(Collections.emptyList())
                .build();

        assertEquals(5, booking.getBookingId());
        assertEquals("PNR999", booking.getPnrNumber());
        assertEquals(3, booking.getNumberOfPassengers());
        assertEquals(new BigDecimal("500.00"), booking.getTotalPrice());
        assertEquals("CANCELLED", booking.getBookingStatus());
        assertEquals("ROUND_TRIP", booking.getTripType());
        assertFalse(booking.getIsActive());
        assertEquals(now, booking.getBookingDate());
        assertEquals(now, booking.getUpdatedAt());
        assertEquals(now.minusDays(1), booking.getCreatedAt());
        assertNotNull(booking.getPassengers());
    }

    @Test
    void testEqualsAndHashCodeFromLombokData() {
        Booking b1 = new Booking();
        b1.setBookingId(1);
        b1.setPnrNumber("PNR123");

        Booking b2 = new Booking();
        b2.setBookingId(1);
        b2.setPnrNumber("PNR123");

        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
        assertNotNull(b1.toString());   // covers toString
    }

    @Test
    void testPrePersistSetsDatesAndDefaultIsActiveWhenNull() {
        Booking booking = new Booking();
        booking.setIsActive(null);     // so the branch "if (isActive == null)" is executed

        assertNull(booking.getCreatedAt());
        assertNull(booking.getUpdatedAt());
        assertNull(booking.getBookingDate());

        booking.prePersist();

        assertNotNull(booking.getCreatedAt());
        assertNotNull(booking.getUpdatedAt());
        assertNotNull(booking.getBookingDate());
        assertTrue(booking.getIsActive());    // should be set to true
    }

    @Test
    void testPrePersistDoesNotOverrideIsActiveWhenAlreadySet() {
        Booking booking = new Booking();
        booking.setIsActive(false);

        booking.prePersist();

        // isActive should stay false
        assertFalse(booking.getIsActive());
    }

    @Test
    void testPreUpdateUpdatesUpdatedAt() {
        Booking booking = new Booking();
        booking.setUpdatedAt(LocalDateTime.now().minusDays(1));

        LocalDateTime beforeUpdate = booking.getUpdatedAt();
        booking.preUpdate();

        assertNotNull(booking.getUpdatedAt());
        assertTrue(booking.getUpdatedAt().isAfter(beforeUpdate));
    }
}
