package com.flightapp.service;

import com.flightapp.dto.BookingRequestDTO;
import com.flightapp.dto.BookingResponseDTO;
import com.flightapp.dto.PassengerDTO;
import com.flightapp.entity.*;
import com.flightapp.repository.*;
import com.flightapp.exception.ResourceNotFoundException;
import com.flightapp.exception.BookingException;
import com.flightapp.validation.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final PassengerRepository passengerRepository;
    
    @Transactional
    public BookingResponseDTO bookFlight(Integer flightId, BookingRequestDTO request) {
        log.info("Processing booking for flight {} with email {}", flightId, request.getUserEmail());
        
        if (flightId == null || flightId <= 0) {
            throw new BookingException("Invalid flight ID");
        }
        
        //Fetch or create user
        User user = userRepository.findByEmail(request.getUserEmail())
            .orElseGet(() -> createGuestUser(request.getUserEmail()));
        
        //Fetch flight
        Flight flight = flightRepository.findById(flightId)
            .orElseThrow(() -> new ResourceNotFoundException("Flight not found with ID: " + flightId));
        
        //Validate booking
        validateBookingRequest(flight, request);
        
        //Create booking
        String pnrNumber = generatePNR();
        BigDecimal totalPrice = flight.getPricePerSeat().multiply(
            BigDecimal.valueOf(request.getNumberOfPassengers())
        );
        
        Booking booking = Booking.builder()
            .pnrNumber(pnrNumber)
            .user(user)
            .flight(flight)
            .numberOfPassengers(request.getNumberOfPassengers())
            .totalPrice(totalPrice)
            .bookingStatus("CONFIRMED")
            .tripType(request.getTripType())
            .build();
        
        //Add passengers
        List<Passenger> passengers = createPassengers(booking, request.getPassengers());
        booking.setPassengers(passengers);
        
        //Save booking
        Booking savedBooking = bookingRepository.save(booking);
        
        //Update flight available seats
        flight.setAvailableSeats(flight.getAvailableSeats() - request.getNumberOfPassengers());
        flightRepository.save(flight);
        
        log.info("Booking created successfully with PNR: {}", pnrNumber);
        return convertToDTO(savedBooking);
    }
    
    @Transactional
    public BookingResponseDTO cancelBooking(String pnrNumber) {
        log.info("Cancelling booking with PNR: {}", pnrNumber);
        
        Booking booking = bookingRepository.findByPnrNumber(pnrNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found with PNR: " + pnrNumber));
        
        if ("CANCELLED".equals(booking.getBookingStatus())) {
            throw new BookingException("Booking is already cancelled");
        }
        
        //Check if cancellation is within 24 hours
        LocalDateTime departureTime = booking.getFlight().getDepartureTime();
        if (LocalDateTime.now().isAfter(departureTime.minusHours(24))) {
            throw new BookingException("Cannot cancel booking within 24 hours of departure");
        }
        
        booking.setBookingStatus("CANCELLED");
        booking.setCancellationDate(LocalDateTime.now());
        booking.setRefundAmount(booking.getTotalPrice());
        bookingRepository.save(booking);
        
        //Restore available seats
        Flight flight = booking.getFlight();
        flight.setAvailableSeats(flight.getAvailableSeats() + booking.getNumberOfPassengers());
        flightRepository.save(flight);
        
        log.info("Booking cancelled successfully");
        return convertToDTO(booking);
    }
    
    @Transactional(readOnly = true)
    public BookingResponseDTO getBookingByPNR(String pnrNumber) {
        log.info("Fetching booking with PNR: {}", pnrNumber);
        
        Booking booking = bookingRepository.findByPnrNumber(pnrNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        return convertToDTO(booking);
    }
    
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingHistory(String email) {
        log.info("Fetching booking history for email: {}", email);
        
        ValidationUtils.validateEmail(email);
        List<Booking> bookings = bookingRepository.findByUserEmail(email);
        return bookings.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    private User createGuestUser(String email) {
        log.info("Creating guest user for email: {}", email);
        
        User guestUser = User.builder()
            .email(email)
            .password("guest_" + System.currentTimeMillis())
            .firstName("Guest")
            .lastName("User")
            .isActive(true)
            .build();
        
        return userRepository.save(guestUser);
    }
    
    private void validateBookingRequest(Flight flight, BookingRequestDTO request) {
        ValidationUtils.validateNumberOfPassengers(request.getNumberOfPassengers());
        ValidationUtils.validateAvailableSeats(flight.getAvailableSeats(), request.getNumberOfPassengers());
        
        if (request.getPassengers().size() != request.getNumberOfPassengers()) {
            throw new BookingException("Number of passengers doesn't match passenger details count");
        }
        
        if (LocalDateTime.now().isAfter(flight.getDepartureTime())) {
            throw new BookingException("Cannot book flight that has already departed");
        }
    }
    
    private List<Passenger> createPassengers(Booking booking, List<PassengerDTO> passengerDTOs) {
        return passengerDTOs.stream()
            .map(dto -> {
                ValidationUtils.validateName(dto.getPassengerName());
                ValidationUtils.validateAge(dto.getAge());
                
                return Passenger.builder()
                    .booking(booking)
                    .passengerName(dto.getPassengerName())
                    .gender(dto.getGender())
                    .age(dto.getAge())
                    .mealPreference(dto.getMealPreference())
                    .isActive(true)
                    .baggageAllowanceKg(20)
                    .build();
            })
            .collect(Collectors.toList());
    }
    
    private String generatePNR() {
        return "PNR" + String.format("%07d", System.currentTimeMillis() % 10000000);
    }
    
    private BookingResponseDTO convertToDTO(Booking booking) {
        return BookingResponseDTO.builder()
            .bookingId(booking.getBookingId())
            .pnrNumber(booking.getPnrNumber())
            .flightNumber(booking.getFlight().getFlightNumber())
            .departureCity(booking.getFlight().getDepartureCity())
            .arrivalCity(booking.getFlight().getArrivalCity())
            .departureTime(booking.getFlight().getDepartureTime())
            .totalPrice(booking.getTotalPrice())
            .bookingStatus(booking.getBookingStatus())
            .bookingDate(booking.getBookingDate())
            .numberOfPassengers(booking.getNumberOfPassengers())
            .build();
    }
}
