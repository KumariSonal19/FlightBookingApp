package com.flightapp.controller;

import com.flightapp.dto.BookingRequestDTO;
import com.flightapp.dto.BookingResponseDTO;
import com.flightapp.exception.ValidationException;
import com.flightapp.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1.0/flight/booking")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class BookingController {
    
    private final BookingService bookingService;
    
    @PostMapping("/{flightId}")
    public ResponseEntity<BookingResponseDTO> bookFlight(
        @PathVariable Integer flightId,
        @RequestBody BookingRequestDTO request) {
        
        log.info("Processing booking for flight {}", flightId);
        
        if (flightId == null || flightId <= 0) {
            throw new ValidationException("Invalid flight ID");
        }
        
        if (request == null) {
            throw new ValidationException("Booking request cannot be null");
        }
        if (request.getUserEmail() == null || request.getUserEmail().trim().isEmpty()) {
            throw new ValidationException("User email is required");
        }
        if (request.getNumberOfPassengers() == null || request.getNumberOfPassengers() <= 0) {
            throw new ValidationException("Number of passengers must be greater than 0");
        }
        if (request.getPassengers() == null || request.getPassengers().isEmpty()) {
            throw new ValidationException("Passenger details are required");
        }
        if (request.getPassengers().size() != request.getNumberOfPassengers()) {
            throw new ValidationException("Passenger count mismatch");
        }
        
        BookingResponseDTO booking = bookingService.bookFlight(flightId, request);
        log.info("Booking created with PNR: {}", booking.getPnrNumber());
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }
    
    @GetMapping("/ticket/{pnr}")
    public ResponseEntity<BookingResponseDTO> getTicket(@PathVariable String pnr) {
        log.info("Fetching booking for PNR: {}", pnr);
        
        if (pnr == null || pnr.trim().isEmpty()) {
            throw new ValidationException("PNR cannot be empty");
        }
        
        BookingResponseDTO booking = bookingService.getBookingByPNR(pnr);
        return ResponseEntity.ok(booking);
    }
    
    @GetMapping("/history/{emailId}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingHistory(@PathVariable String emailId) {
        log.info("Fetching booking history for email: {}", emailId);
        
        if (emailId == null || emailId.trim().isEmpty()) {
            throw new ValidationException("Email cannot be empty");
        }
        
        List<BookingResponseDTO> bookings = bookingService.getBookingHistory(emailId);
        log.info("Found {} bookings for user", bookings.size());
        return ResponseEntity.ok(bookings);
    }
    
    @DeleteMapping("/cancel/{pnr}")
    public ResponseEntity<Map<String, Object>> cancelBooking(@PathVariable String pnr) {
        log.info("Cancelling booking with PNR: {}", pnr);
        
        if (pnr == null || pnr.trim().isEmpty()) {
            throw new ValidationException("PNR cannot be empty");
        }
        
        BookingResponseDTO cancelledBooking = bookingService.cancelBooking(pnr);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Booking cancelled successfully");
        response.put("pnrNumber", cancelledBooking.getPnrNumber());
        response.put("refundAmount", cancelledBooking.getTotalPrice());
        
        log.info("Booking cancelled: {}", pnr);
        return ResponseEntity.ok(response);
    }
}
