package com.flightapp.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Booking not found");

        ResponseEntity<ErrorResponse> response =
                handler.handleResourceNotFoundException(ex, (WebRequest) null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.NOT_FOUND.value(), body.getStatus());
        assertEquals("Booking not found", body.getMessage());
        assertEquals("Resource Not Found", body.getError());
        assertNotNull(body.getTimestamp());
    }

    @Test
    void testHandleBookingException() {
        BookingException ex = new BookingException("Invalid booking");

        ResponseEntity<ErrorResponse> response =
                handler.handleBookingException(ex, (WebRequest) null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus());
        assertEquals("Invalid booking", body.getMessage());
        assertEquals("Booking Error", body.getError());
        assertNotNull(body.getTimestamp());
    }

    @Test
    void testHandleValidationException() {
        ValidationException ex = new ValidationException("Validation failed");

        ResponseEntity<ErrorResponse> response =
                handler.handleValidationException(ex, (WebRequest) null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus());
        assertEquals("Validation failed", body.getMessage());
        assertEquals("Validation Error", body.getError());
        assertNotNull(body.getTimestamp());
    }

    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Bad arg");

        ResponseEntity<ErrorResponse> response =
                handler.handleIllegalArgumentException(ex, (WebRequest) null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus());
        assertEquals("Bad arg", body.getMessage());
        assertEquals("Invalid Argument", body.getError());
        assertNotNull(body.getTimestamp());
    }

    @Test
    void testHandleGeneralException() {
        Exception ex = new RuntimeException("Boom");

        ResponseEntity<ErrorResponse> response =
                handler.handleGeneralException(ex, (WebRequest) null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.getStatus());
        // Global handler uses a fixed message here
        assertEquals("An unexpected error occurred", body.getMessage());
        assertEquals(ex.getClass().getSimpleName(), body.getError());
        assertNotNull(body.getTimestamp());
    }
}
