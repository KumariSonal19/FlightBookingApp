package com.flightapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class FlightBookingAppApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(FlightBookingAppApplication.class, args);
        log.info("Flight Booking System started successfully");
    }
}
