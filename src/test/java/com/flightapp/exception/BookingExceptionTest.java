package com.flightapp.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookingExceptionTest {

    @Test
    void testMessageConstructor() {
        BookingException ex = new BookingException("Booking error");
        assertEquals("Booking error", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void testMessageAndCauseConstructor() {
        Throwable cause = new RuntimeException("root cause");

        BookingException ex = new BookingException("Booking error", cause);

        assertEquals("Booking error", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
