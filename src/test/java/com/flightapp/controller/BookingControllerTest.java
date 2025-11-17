package com.flightapp.controller;
import com.flightapp.dto.BookingRequestDTO;
import com.flightapp.dto.BookingResponseDTO;
import com.flightapp.dto.PassengerDTO;
import com.flightapp.exception.BookingException;
import com.flightapp.exception.ResourceNotFoundException;
import com.flightapp.exception.ValidationException;
import com.flightapp.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    
    @Mock
    private BookingService bookingService;
    
    @InjectMocks
    private BookingController bookingController;
    
    private BookingRequestDTO bookingRequest;
    private BookingResponseDTO bookingResponse;
    
    @BeforeEach
    public void setUp() {
        PassengerDTO passenger = new PassengerDTO();
        passenger.setPassengerName("John Doe");
        passenger.setGender("Male");
        passenger.setAge(30);
        passenger.setMealPreference("Vegetarian");
        
        bookingRequest = BookingRequestDTO.builder()
            .userEmail("john@example.com")
            .numberOfPassengers(1)
            .passengers(Arrays.asList(passenger))
            .tripType("ONE_WAY")
            .build();
        
        bookingResponse = BookingResponseDTO.builder()
            .bookingId(1)
            .pnrNumber("PNR0012345")
            .flightNumber("AI101")
            .departureCity("Delhi")
            .arrivalCity("Mumbai")
            .departureTime(LocalDateTime.now().plusHours(2))
            .totalPrice(BigDecimal.valueOf(5000))
            .bookingStatus("CONFIRMED")
            .bookingDate(LocalDateTime.now())
            .numberOfPassengers(1)
            .build();
    }
    
    @Test
    void testBookFlight_Success() {
        when(bookingService.bookFlight(anyInt(), any(BookingRequestDTO.class)))
            .thenReturn(bookingResponse);
        
        ResponseEntity<BookingResponseDTO> response = bookingController.bookFlight(1, bookingRequest);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("PNR0012345", response.getBody().getPnrNumber());
        assertEquals("CONFIRMED", response.getBody().getBookingStatus());
        verify(bookingService, times(1)).bookFlight(anyInt(), any());
    }
    
    @Test
    void testBookFlight_InvalidFlightId() {
        assertThrows(ValidationException.class, () -> bookingController.bookFlight(-1, bookingRequest));
    }
    
    @Test
    void testBookFlight_NullEmail() {
        bookingRequest.setUserEmail(null);
        
        assertThrows(ValidationException.class, () -> bookingController.bookFlight(1, bookingRequest));
    }
    
    @Test
    void testBookFlight_InvalidPassengerCount() {
        bookingRequest.setNumberOfPassengers(2);
        
        assertThrows(ValidationException.class, () -> bookingController.bookFlight(1, bookingRequest));
    }
    
    @Test
    void testGetTicket_Success() {
        when(bookingService.getBookingByPNR("PNR0012345"))
            .thenReturn(bookingResponse);
        
        ResponseEntity<BookingResponseDTO> response = bookingController.getTicket("PNR0012345");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("PNR0012345", response.getBody().getPnrNumber());
    }
    
    @Test
    void testGetTicket_NotFound() {
        when(bookingService.getBookingByPNR("INVALID"))
            .thenThrow(new ResourceNotFoundException("Booking not found"));
        
        assertThrows(ResourceNotFoundException.class, () -> bookingController.getTicket("INVALID"));
    }
    
    @Test
    void testGetBookingHistory_Success() {
        List<BookingResponseDTO> bookings = Arrays.asList(bookingResponse);
        when(bookingService.getBookingHistory("john@example.com"))
            .thenReturn(bookings);
        
        ResponseEntity<List<BookingResponseDTO>> response = bookingController.getBookingHistory("john@example.com");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }
    
    @Test
    void testGetBookingHistory_Empty() {
        when(bookingService.getBookingHistory("unknown@example.com"))
            .thenReturn(Arrays.asList());
        
        ResponseEntity<List<BookingResponseDTO>> response = bookingController.getBookingHistory("unknown@example.com");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }
    
    @Test
    void testCancelBooking_Success() {
        when(bookingService.cancelBooking("PNR0012345"))
            .thenReturn(bookingResponse);
        
        ResponseEntity<Map<String, Object>> response = bookingController.cancelBooking("PNR0012345");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("message"));
        assertEquals("Booking cancelled successfully", response.getBody().get("message"));
    }
    
    @Test
    void testCancelBooking_WithinCancellationWindow() {
        when(bookingService.cancelBooking("PNR0012345"))
            .thenThrow(new BookingException("Cannot cancel booking within 24 hours"));
        
        assertThrows(BookingException.class, () -> bookingController.cancelBooking("PNR0012345"));
    }
    
    @Test
    void testCancelBooking_NotFound() {
        when(bookingService.cancelBooking("INVALID"))
            .thenThrow(new ResourceNotFoundException("Booking not found"));
        
        assertThrows(ResourceNotFoundException.class, () -> bookingController.cancelBooking("INVALID"));
    }
    
    @Test
    void testBookFlight_NullRequest() {
        assertThrows(ValidationException.class, () -> bookingController.bookFlight(1, null));
    }
    
    @Test
    void testGetTicket_EmptyPNR() {
        assertThrows(ValidationException.class, () -> bookingController.getTicket(""));
    }
    
    @Test
    void testGetBookingHistory_NullEmail() {
        assertThrows(ValidationException.class, () -> bookingController.getBookingHistory(null));
    }
    
    @Test
    void testCancelBooking_EmptyPNR() {
        assertThrows(ValidationException.class, () -> bookingController.cancelBooking(""));
    }
    
    @Test
    void testBookFlight_MultiplPassengers() {
        PassengerDTO passenger2 = new PassengerDTO();
        passenger2.setPassengerName("Jane Doe");
        passenger2.setGender("Female");
        passenger2.setAge(28);
        passenger2.setMealPreference("Non-Vegetarian");
        
        bookingRequest.setNumberOfPassengers(2);
        bookingRequest.setPassengers(Arrays.asList(
            bookingRequest.getPassengers().get(0),
            passenger2
        ));
        
        when(bookingService.bookFlight(anyInt(), any(BookingRequestDTO.class)))
            .thenReturn(bookingResponse);
        
        ResponseEntity<BookingResponseDTO> response = bookingController.bookFlight(1, bookingRequest);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
