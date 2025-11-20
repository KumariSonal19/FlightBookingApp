package com.flightapp.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class AirlineTest {

    @Test
    void testNoArgsConstructorAndDefaults() {
        Airline airline = new Airline();   // uses default field values

        // default value from field initialiser
        assertTrue(airline.getIsActive());

        // setters / getters
        airline.setAirlineId(1);
        airline.setAirlineName("Test Airline");
        airline.setAirlineCode("TA");
        airline.setLogoUrl("http://logo.url");
        airline.setContactNumber("1234567890");
        airline.setEmail("test@airline.com");

        assertEquals(1, airline.getAirlineId());
        assertEquals("Test Airline", airline.getAirlineName());
        assertEquals("TA", airline.getAirlineCode());
        assertEquals("http://logo.url", airline.getLogoUrl());
        assertEquals("1234567890", airline.getContactNumber());
        assertEquals("test@airline.com", airline.getEmail());
    }

    @Test
    void testBuilder() {
        LocalDateTime now = LocalDateTime.now();

        Airline airline = Airline.builder()
                .airlineId(2)
                .airlineName("Builder Airline")
                .airlineCode("BA")
                .logoUrl("http://builder.logo")
                .contactNumber("0987654321")
                .email("builder@airline.com")
                .isActive(false)
                .createdAt(now.minusDays(1))
                .updatedAt(now)
                .flights(Collections.emptyList())
                .build();

        assertEquals(2, airline.getAirlineId());
        assertEquals("Builder Airline", airline.getAirlineName());
        assertEquals("BA", airline.getAirlineCode());
        assertEquals("http://builder.logo", airline.getLogoUrl());
        assertEquals("0987654321", airline.getContactNumber());
        assertEquals("builder@airline.com", airline.getEmail());
        assertFalse(airline.getIsActive());
        assertEquals(now.minusDays(1), airline.getCreatedAt());
        assertEquals(now, airline.getUpdatedAt());
        assertNotNull(airline.getFlights());
    }

    @Test
    void testEqualsHashCodeAndToString() {
        Airline a1 = new Airline();
        a1.setAirlineId(1);
        a1.setAirlineName("Same");
        a1.setAirlineCode("SC");

        Airline a2 = new Airline();
        a2.setAirlineId(1);
        a2.setAirlineName("Same");
        a2.setAirlineCode("SC");

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
        assertNotNull(a1.toString());
    }

    @Test
    void testPrePersistSetsTimestampsAndDefaultIsActiveWhenNull() {
        Airline airline = new Airline();
        airline.setIsActive(null);   // so the if (isActive == null) branch is used

        assertNull(airline.getCreatedAt());
        assertNull(airline.getUpdatedAt());

        airline.prePersist();

        assertNotNull(airline.getCreatedAt());
        assertNotNull(airline.getUpdatedAt());
        assertTrue(airline.getIsActive());
    }

    @Test
    void testPrePersistDoesNotChangeIsActiveIfAlreadySet() {
        Airline airline = new Airline();
        airline.setIsActive(false);

        airline.prePersist();

        assertFalse(airline.getIsActive());  // remains false
    }

    @Test
    void testPreUpdateUpdatesUpdatedAt() {
        Airline airline = new Airline();
        airline.setUpdatedAt(LocalDateTime.now().minusDays(1));

        LocalDateTime before = airline.getUpdatedAt();

        airline.preUpdate();

        assertNotNull(airline.getUpdatedAt());
        assertTrue(airline.getUpdatedAt().isAfter(before));
    }
}
