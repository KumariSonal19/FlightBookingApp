package com.flightapp.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SeatTest {

    @Test
    void testNoArgsConstructorAndDefaults() {
        Seat seat = new Seat();

        // Default from field initializer
        assertTrue(seat.getIsActive());

        // Setters / getters
        Flight flight = new Flight();
        flight.setFlightId(1);

        Booking booking = new Booking();
        booking.setBookingId(99);

        seat.setSeatId(10);
        seat.setFlight(flight);
        seat.setSeatNumber("12A");
        seat.setSeatClass("ECONOMY");
        seat.setIsAvailable(true);
        seat.setIsReserved(false);
        seat.setBooking(booking);
        seat.setReservedAt(LocalDateTime.now());

        assertEquals(10, seat.getSeatId());
        assertEquals(flight, seat.getFlight());
        assertEquals("12A", seat.getSeatNumber());
        assertEquals("ECONOMY", seat.getSeatClass());
        assertTrue(seat.getIsAvailable());
        assertFalse(seat.getIsReserved());
        assertEquals(booking, seat.getBooking());
        assertNotNull(seat.getReservedAt());
    }

    @Test
    void testBuilder() {
        Flight flight = Flight.builder()
                .flightId(2)
                .flightNumber("FL999")
                .build();

        Booking booking = Booking.builder()
                .bookingId(11)
                .pnrNumber("PNR100")
                .build();

        LocalDateTime now = LocalDateTime.now();

        Seat seat = Seat.builder()
                .seatId(5)
                .flight(flight)
                .seatNumber("1B")
                .seatClass("BUSINESS")
                .isAvailable(false)
                .isReserved(true)
                .booking(booking)
                .reservedAt(now)
                .isActive(false)
                .createdAt(now.minusDays(1))
                .updatedAt(now)
                .build();

        assertEquals(5, seat.getSeatId());
        assertEquals(flight, seat.getFlight());
        assertEquals("1B", seat.getSeatNumber());
        assertEquals("BUSINESS", seat.getSeatClass());
        assertFalse(seat.getIsAvailable());
        assertTrue(seat.getIsReserved());
        assertEquals(booking, seat.getBooking());
        assertEquals(now, seat.getReservedAt());
        assertFalse(seat.getIsActive());
        assertEquals(now.minusDays(1), seat.getCreatedAt());
        assertEquals(now, seat.getUpdatedAt());
    }

    @Test
    void testEqualsHashCodeAndToString() {
        Seat s1 = new Seat();
        s1.setSeatId(1);
        s1.setSeatNumber("12A");

        Seat s2 = new Seat();
        s2.setSeatId(1);
        s2.setSeatNumber("12A");

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotNull(s1.toString());
    }

    @Test
    void testPrePersistSetsTimestampsAndDefaultsWhenNull() {
        Seat seat = new Seat();

        seat.setIsAvailable(null);  // triggers branch
        seat.setIsReserved(null);   // triggers branch
        seat.setIsActive(null);     // triggers branch

        assertNull(seat.getCreatedAt());
        assertNull(seat.getUpdatedAt());

        seat.prePersist();

        assertNotNull(seat.getCreatedAt());
        assertNotNull(seat.getUpdatedAt());

        assertTrue(seat.getIsAvailable());
        assertFalse(seat.getIsReserved());
        assertTrue(seat.getIsActive());
    }

    @Test
    void testPrePersistDoesNotOverrideNonNullValues() {
        Seat seat = new Seat();

        seat.setIsAvailable(false);
        seat.setIsReserved(true);
        seat.setIsActive(false);

        seat.prePersist();

        assertFalse(seat.getIsAvailable());
        assertTrue(seat.getIsReserved());
        assertFalse(seat.getIsActive());
    }

    @Test
    void testPreUpdateUpdatesUpdatedAt() {
        Seat seat = new Seat();
        LocalDateTime before = LocalDateTime.now().minusHours(1);
        seat.setUpdatedAt(before);

        seat.preUpdate();

        assertNotNull(seat.getUpdatedAt());
        assertTrue(seat.getUpdatedAt().isAfter(before));
    }
}
