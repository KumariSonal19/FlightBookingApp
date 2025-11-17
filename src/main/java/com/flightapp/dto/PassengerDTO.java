package com.flightapp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDTO {
    private String passengerName;
    private String gender;
    private Integer age;
    private String mealPreference;
}
