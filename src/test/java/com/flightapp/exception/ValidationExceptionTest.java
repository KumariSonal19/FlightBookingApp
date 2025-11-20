package com.flightapp.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {

    @Test
    void testMessageConstructor() {
        ValidationException ex = new ValidationException("Invalid input");
        assertEquals("Invalid input", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void testMessageAndCauseConstructor() {
        Throwable cause = new RuntimeException("Root cause");

        ValidationException ex =
                new ValidationException("Invalid input", cause);

        assertEquals("Invalid input", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
