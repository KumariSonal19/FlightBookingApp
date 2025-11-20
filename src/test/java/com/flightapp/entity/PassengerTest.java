package com.flightapp.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PassengerTest {

    @Test
    void testNoArgsConstructorAndDefaults() {
        Passenger passenger = new Passenger();   // uses default values

        // default from field initialiser
        assertTrue(passenger.getIsActive());

        // setters / getters
        Booking booking = new Booking();
        booking.setBookingId(1);

        passenger.setPassengerId(10);
        passenger.setBooking(booking);
        passenger.setPassengerName("John Doe");
        passenger.setGender("Male");
        passenger.setAge(30);
        passenger.setDateOfBirth(LocalDate.of(1994, 1, 1));
        passenger.setEmail("john@example.com");
        passenger.setPhoneNumber("1234567890");
        passenger.setMealPreference("VEG");
        passenger.setSeatNumber("12A");
        passenger.setBaggageAllowanceKg(25);

        assertEquals(10, passenger.getPassengerId());
        assertEquals(booking, passenger.getBooking());
        assertEquals("John Doe", passenger.getPassengerName());
        assertEquals("Male", passenger.getGender());
        assertEquals(30, passenger.getAge());
        assertEquals(LocalDate.of(1994, 1, 1), passenger.getDateOfBirth());
        assertEquals("john@example.com", passenger.getEmail());
        assertEquals("1234567890", passenger.getPhoneNumber());
        assertEquals("VEG", passenger.getMealPreference());
        assertEquals("12A", passenger.getSeatNumber());
        assertEquals(25, passenger.getBaggageAllowanceKg());
    }

    @Test
    void testBuilder() {
        Booking booking = Booking.builder()
                .bookingId(2)
                .pnrNumber("PNR123")
                .build();

        LocalDateTime now = LocalDateTime.now();

        Passenger passenger = Passenger.builder()
                .passengerId(20)
                .booking(booking)
                .passengerName("Builder User")
                .gender("F")
                .age(28)
                .dateOfBirth(LocalDate.of(1996, 2, 2))
                .email("builder@example.com")
                .phoneNumber("9876543210")
                .mealPreference("NON_VEG")
                .seatNumber("5B")
                .baggageAllowanceKg(30)
                .isActive(false)
                .createdAt(now.minusDays(1))
                .updatedAt(now)
                .build();

        assertEquals(20, passenger.getPassengerId());
        assertEquals(booking, passenger.getBooking());
        assertEquals("Builder User", passenger.getPassengerName());
        assertEquals("F", passenger.getGender());
        assertEquals(28, passenger.getAge());
        assertEquals(LocalDate.of(1996, 2, 2), passenger.getDateOfBirth());
        assertEquals("builder@example.com", passenger.getEmail());
        assertEquals("9876543210", passenger.getPhoneNumber());
        assertEquals("NON_VEG", passenger.getMealPreference());
        assertEquals("5B", passenger.getSeatNumber());
        assertEquals(30, passenger.getBaggageAllowanceKg());
        assertFalse(passenger.getIsActive());
        assertEquals(now.minusDays(1), passenger.getCreatedAt());
        assertEquals(now, passenger.getUpdatedAt());
    }

    @Test
    void testEqualsHashCodeAndToString() {
        Passenger p1 = new Passenger();
        p1.setPassengerId(1);
        p1.setPassengerName("Same");

        Passenger p2 = new Passenger();
        p2.setPassengerId(1);
        p2.setPassengerName("Same");

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotNull(p1.toString());
    }

    @Test
    void testPrePersistSetsTimestampsAndDefaultsWhenNull() {
        Passenger passenger = new Passenger();
        passenger.setIsActive(null);           // hit isActive == null branch
        passenger.setBaggageAllowanceKg(null); // hit baggageAllowanceKg == null branch

        assertNull(passenger.getCreatedAt());
        assertNull(passenger.getUpdatedAt());
        assertNull(passenger.getBaggageAllowanceKg());

        passenger.prePersist();

        assertNotNull(passenger.getCreatedAt());
        assertNotNull(passenger.getUpdatedAt());
        assertTrue(passenger.getIsActive());
        assertEquals(20, passenger.getBaggageAllowanceKg());
    }

    @Test
    void testPrePersistDoesNotOverrideNonNullValues() {
        Passenger passenger = new Passenger();
        passenger.setIsActive(false);
        passenger.setBaggageAllowanceKg(30);

        passenger.prePersist();

        // both should remain as set
        assertFalse(passenger.getIsActive());
        assertEquals(30, passenger.getBaggageAllowanceKg());
    }

    @Test
    void testPreUpdateUpdatesUpdatedAt() {
        Passenger passenger = new Passenger();
        passenger.setUpdatedAt(LocalDateTime.now().minusDays(1));

        LocalDateTime before = passenger.getUpdatedAt();
        passenger.preUpdate();

        assertNotNull(passenger.getUpdatedAt());
        assertTrue(passenger.getUpdatedAt().isAfter(before));
    }
}
