package com.flightapp.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PassengerDTOTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        PassengerDTO dto = new PassengerDTO();

        dto.setPassengerName("John Doe");
        dto.setGender("Male");
        dto.setAge(30);
        dto.setMealPreference("VEG");

        assertEquals("John Doe", dto.getPassengerName());
        assertEquals("Male", dto.getGender());
        assertEquals(30, dto.getAge());
        assertEquals("VEG", dto.getMealPreference());
    }

    @Test
    void testAllArgsConstructor() {
        PassengerDTO dto = new PassengerDTO(
                "Jane Doe",
                "Female",
                25,
                "NON_VEG"
        );

        assertEquals("Jane Doe", dto.getPassengerName());
        assertEquals("Female", dto.getGender());
        assertEquals(25, dto.getAge());
        assertEquals("NON_VEG", dto.getMealPreference());
    }

    @Test
    void testEqualsHashCodeAndToString() {
        PassengerDTO p1 = new PassengerDTO("Same", "X", 20, "MEAL");
        PassengerDTO p2 = new PassengerDTO("Same", "X", 20, "MEAL");

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotNull(p1.toString());
    }
}
