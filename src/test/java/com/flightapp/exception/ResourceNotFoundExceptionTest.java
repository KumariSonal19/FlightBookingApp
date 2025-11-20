package com.flightapp.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void testMessageConstructor() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found");
        assertEquals("Not found", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void testMessageAndCauseConstructor() {
        Throwable cause = new RuntimeException("Root cause");

        ResourceNotFoundException ex =
                new ResourceNotFoundException("Not found", cause);

        assertEquals("Not found", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
