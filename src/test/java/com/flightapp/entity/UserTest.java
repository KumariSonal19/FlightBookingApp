package com.flightapp.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testNoArgsConstructorAndDefaults() {
        User user = new User();   // uses field defaults

        // defaults from field initialisers
        assertEquals("USER", user.getRole());
        assertTrue(user.isActive());

        // setters / getters
        user.setUserId(1);
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhoneNumber("1234567890");
        user.setAadharNumber("123412341234");
        user.setAddress("Some address");
        user.setCity("Some City");
        user.setState("Some State");
        user.setPincode("123456");

        assertEquals(1, user.getUserId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("1234567890", user.getPhoneNumber());
        assertEquals("123412341234", user.getAadharNumber());
        assertEquals("Some address", user.getAddress());
        assertEquals("Some City", user.getCity());
        assertEquals("Some State", user.getState());
        assertEquals("123456", user.getPincode());
    }

    @Test
    void testBuilder() {
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .userId(2)
                .email("builder@example.com")
                .password("secret")
                .firstName("Builder")
                .lastName("User")
                .phoneNumber("9999999999")
                .aadharNumber("999999999999")
                .address("Builder Address")
                .city("Builder City")
                .state("Builder State")
                .pincode("654321")
                .role("ADMIN")
                .isActive(false)
                .createdAt(now.minusDays(1))
                .updatedAt(now)
                .bookings(Collections.emptyList())
                .build();

        assertEquals(2, user.getUserId());
        assertEquals("builder@example.com", user.getEmail());
        assertEquals("secret", user.getPassword());
        assertEquals("Builder", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertEquals("9999999999", user.getPhoneNumber());
        assertEquals("999999999999", user.getAadharNumber());
        assertEquals("Builder Address", user.getAddress());
        assertEquals("Builder City", user.getCity());
        assertEquals("Builder State", user.getState());
        assertEquals("654321", user.getPincode());
        assertEquals("ADMIN", user.getRole());
        assertFalse(user.isActive());
        assertEquals(now.minusDays(1), user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
        assertNotNull(user.getBookings());
    }

    @Test
    void testEqualsHashCodeAndToString() {
        User u1 = new User();
        u1.setUserId(1);
        u1.setEmail("same@example.com");

        User u2 = new User();
        u2.setUserId(1);
        u2.setEmail("same@example.com");

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
        assertNotNull(u1.toString());
    }

    @Test
    void testPrePersistSetsTimestamps() {
        User user = new User();

        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());

        user.prePersist();

        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    void testPreUpdateUpdatesUpdatedAt() {
        User user = new User();
        LocalDateTime before = LocalDateTime.now().minusHours(1);
        user.setUpdatedAt(before);

        user.preUpdate();

        assertNotNull(user.getUpdatedAt());
        assertTrue(user.getUpdatedAt().isAfter(before));
    }
}
