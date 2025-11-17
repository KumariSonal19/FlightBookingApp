package com.flightapp.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDTO {
    private Integer bookingId;
    private String pnrNumber;
    private String flightNumber;
    private String departureCity;
    private String arrivalCity;
    private LocalDateTime departureTime;
    private BigDecimal totalPrice;
    private String bookingStatus;
    private LocalDateTime bookingDate;
    private Integer numberOfPassengers;
}
