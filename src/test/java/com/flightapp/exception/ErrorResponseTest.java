package com.flightapp.exception;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        ErrorResponse er = new ErrorResponse();

        LocalDateTime now = LocalDateTime.now();

        er.setStatus(400);
        er.setMessage("Bad request");
        er.setError("Validation failed");
        er.setTimestamp(now);

        assertEquals(400, er.getStatus());
        assertEquals("Bad request", er.getMessage());
        assertEquals("Validation failed", er.getError());
        assertEquals(now, er.getTimestamp());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime time = LocalDateTime.now();

        ErrorResponse er = new ErrorResponse(
                404,
                "Not found",
                "Resource missing",
                time
        );

        assertEquals(404, er.getStatus());
        assertEquals("Not found", er.getMessage());
        assertEquals("Resource missing", er.getError());
        assertEquals(time, er.getTimestamp());
    }

    @Test
    void testBuilder() {
        LocalDateTime time = LocalDateTime.now();

        ErrorResponse er = ErrorResponse.builder()
                .status(500)
                .message("Server error")
                .error("NullPointerException")
                .timestamp(time)
                .build();

        assertEquals(500, er.getStatus());
        assertEquals("Server error", er.getMessage());
        assertEquals("NullPointerException", er.getError());
        assertEquals(time, er.getTimestamp());
    }

    @Test
    void testEqualsHashCodeToString() {
        LocalDateTime time = LocalDateTime.now();

        ErrorResponse e1 = ErrorResponse.builder()
                .status(200)
                .message("OK")
                .error("None")
                .timestamp(time)
                .build();

        ErrorResponse e2 = ErrorResponse.builder()
                .status(200)
                .message("OK")
                .error("None")
                .timestamp(time)
                .build();

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
        assertNotNull(e1.toString());
    }
}
