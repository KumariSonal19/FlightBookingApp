package com.flightapp.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequestDTO {
    private String userEmail;
    private Integer numberOfPassengers;
    private List<PassengerDTO> passengers;
    private String tripType;
    private Integer returnFlightId;
}
