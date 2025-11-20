package com.flightapp.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRegistrationDTOTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        UserRegistrationDTO dto = new UserRegistrationDTO();

        dto.setEmail("user@example.com");
        dto.setPassword("password123");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setPhoneNumber("9876543210");
        dto.setAadharNumber("123412341234");
        dto.setAddress("123 Street");
        dto.setCity("Delhi");
        dto.setState("DelhiState");
        dto.setPincode("110001");

        assertEquals("user@example.com", dto.getEmail());
        assertEquals("password123", dto.getPassword());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("9876543210", dto.getPhoneNumber());
        assertEquals("123412341234", dto.getAadharNumber());
        assertEquals("123 Street", dto.getAddress());
        assertEquals("Delhi", dto.getCity());
        assertEquals("DelhiState", dto.getState());
        assertEquals("110001", dto.getPincode());
    }

    @Test
    void testAllArgsConstructor() {
        UserRegistrationDTO dto = new UserRegistrationDTO(
                "test@example.com",
                "pass",
                "Alice",
                "Wonder",
                "1112223334",
                "999988887777",
                "Some address",
                "Mumbai",
                "Maharashtra",
                "400001"
        );

        assertEquals("test@example.com", dto.getEmail());
        assertEquals("pass", dto.getPassword());
        assertEquals("Alice", dto.getFirstName());
        assertEquals("Wonder", dto.getLastName());
        assertEquals("1112223334", dto.getPhoneNumber());
        assertEquals("999988887777", dto.getAadharNumber());
        assertEquals("Some address", dto.getAddress());
        assertEquals("Mumbai", dto.getCity());
        assertEquals("Maharashtra", dto.getState());
        assertEquals("400001", dto.getPincode());
    }

    @Test
    void testEqualsHashCodeAndToString() {
        UserRegistrationDTO u1 = new UserRegistrationDTO(
                "same@example.com",
                "pwd",
                "Same",
                "User",
                "0001112222",
                "123456789000",
                "Addr",
                "City",
                "State",
                "111111"
        );

        UserRegistrationDTO u2 = new UserRegistrationDTO(
                "same@example.com",
                "pwd",
                "Same",
                "User",
                "0001112222",
                "123456789000",
                "Addr",
                "City",
                "State",
                "111111"
        );

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
        assertNotNull(u1.toString());
    }
}
