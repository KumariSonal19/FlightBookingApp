package com.flightapp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String aadharNumber;
    private String address;
    private String city;
    private String state;
    private String pincode;
}
