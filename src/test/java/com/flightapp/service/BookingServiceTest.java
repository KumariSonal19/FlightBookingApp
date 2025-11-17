package com.flightapp.service;

import com.flightapp.dto.BookingRequestDTO;
import com.flightapp.dto.PassengerDTO;
import com.flightapp.entity.*;
import com.flightapp.repository.*;
import com.flightapp.exception.ResourceNotFoundException;
import com.flightapp.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    
    @Mock
    private BookingRepository bookingRepository;
    
    @Mock
    private FlightRepository flightRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PassengerRepository passengerRepository;
    
    @InjectMocks
    private BookingService bookingService;
    
    private User testUser;
    private Flight testFlight;
    private BookingRequestDTO bookingRequest;
    
    @BeforeEach
    public void setUp() {
        testUser = User.builder()
            .userId(1)
            .email("user@example.com")
            .firstName("John")
            .lastName("Doe")
            .role("USER")
            .isActive(true)
            .build();
        
        Airline airline = Airline.builder()
            .airlineId(1)
            .airlineName("Air India")
            .airlineCode("AI")
            .build();
        
        testFlight = Flight.builder()
            .flightId(1)
            .flightNumber("AI101")
            .airline(airline)
            .departureCity("Delhi")
            .arrivalCity("Mumbai")
            .departureTime(LocalDateTime.now().plusHours(2))
            .arrivalTime(LocalDateTime.now().plusHours(4))
            .totalSeats(180)
            .availableSeats(150)
            .pricePerSeat(BigDecimal.valueOf(5000))
            .status("ACTIVE")
            .isActive(true)
            .build();
        
        PassengerDTO passenger = new PassengerDTO();
        passenger.setPassengerName("John Doe");
        passenger.setGender("Male");
        passenger.setAge(30);
        passenger.setMealPreference("Vegetarian");
        
        bookingRequest = BookingRequestDTO.builder()
            .userEmail("user@example.com")
            .numberOfPassengers(1)
            .passengers(Arrays.asList(passenger))
            .tripType("ONE_WAY")
            .build();
    }
    
    @Test
    void testBookFlight_Success() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(testUser));
        when(flightRepository.findById(1)).thenReturn(Optional.of(testFlight));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(flightRepository.save(any(Flight.class))).thenReturn(testFlight);
        
        var result = bookingService.bookFlight(1, bookingRequest);
        
        assertNotNull(result);
        assertNotNull(result.getPnrNumber());
        assertEquals("AI101", result.getFlightNumber());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }
    
    @Test
    void testBookFlight_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(flightRepository.findById(1)).thenReturn(Optional.of(testFlight));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(flightRepository.save(any(Flight.class))).thenReturn(testFlight);
        
        var result = bookingService.bookFlight(1, bookingRequest);
        
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    void testBookFlight_InsufficientSeats() {
      
        testFlight.setAvailableSeats(0);
        bookingRequest.setNumberOfPassengers(5);
        
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(testUser));
        when(flightRepository.findById(1)).thenReturn(Optional.of(testFlight));
        
        assertThrows(ValidationException.class, () -> bookingService.bookFlight(1, bookingRequest));
    }
    
    @Test
    void testGetBookingByPNR_Success() {
        Booking booking = Booking.builder()
            .bookingId(1)
            .pnrNumber("PNR123")
            .flight(testFlight)
            .user(testUser)
            .numberOfPassengers(1)
            .totalPrice(BigDecimal.valueOf(5000))
            .bookingStatus("CONFIRMED")
            .isActive(true)
            .build();
        
        when(bookingRepository.findByPnrNumber("PNR123")).thenReturn(Optional.of(booking));
        
        var result = bookingService.getBookingByPNR("PNR123");
        
        assertNotNull(result);
        assertEquals("PNR123", result.getPnrNumber());
        assertEquals("AI101", result.getFlightNumber());
    }
    
    @Test
    void testGetBookingByPNR_NotFound() {
        when(bookingRepository.findByPnrNumber(anyString())).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> bookingService.getBookingByPNR("INVALID"));
    }
    
    @Test
    void testCancelBooking_Success() {
        
        Flight flightFor2Days = Flight.builder()
            .flightId(1)
            .flightNumber("AI101")
            .airline(testFlight.getAirline())
            .departureCity("Delhi")
            .arrivalCity("Mumbai")
            .departureTime(LocalDateTime.now().plusDays(2))  // 2 days away (>24 hours)
            .arrivalTime(LocalDateTime.now().plusDays(2).plusHours(2))
            .totalSeats(180)
            .availableSeats(151)
            .pricePerSeat(BigDecimal.valueOf(5000))
            .status("ACTIVE")
            .isActive(true)
            .build();
        
        Booking booking = Booking.builder()
            .bookingId(1)
            .pnrNumber("PNR123")
            .flight(flightFor2Days)
            .user(testUser)
            .numberOfPassengers(1)
            .totalPrice(BigDecimal.valueOf(5000))
            .bookingStatus("CONFIRMED")
            .isActive(true)
            .build();
        
        when(bookingRepository.findByPnrNumber("PNR123")).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(flightRepository.save(any(Flight.class))).thenReturn(flightFor2Days);
        
        bookingService.cancelBooking("PNR123");
        
        assertEquals("CANCELLED", booking.getBookingStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }
    
    @Test
    void testGetBookingHistory_Success() {
       
        Booking booking = Booking.builder()
            .bookingId(1)
            .pnrNumber("PNR123")
            .flight(testFlight)
            .user(testUser)
            .numberOfPassengers(1)
            .totalPrice(BigDecimal.valueOf(5000))
            .bookingStatus("CONFIRMED")
            .isActive(true)
            .build();
        
        when(bookingRepository.findByUserEmail("user@example.com"))
            .thenReturn(Arrays.asList(booking));
        
        var result = bookingService.getBookingHistory("user@example.com");
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
