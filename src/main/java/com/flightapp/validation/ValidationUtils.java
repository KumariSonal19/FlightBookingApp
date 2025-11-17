package com.flightapp.validation;

import com.flightapp.exception.ValidationException;
import java.util.regex.Pattern;

public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^[0-9]{10}$");
    
    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be empty");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format");
        }
    }
    
    public static void validatePhoneNumber(String phone) {
        if (phone != null && !phone.isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
            throw new ValidationException("Phone number must be 10 digits");
        }
    }
    
    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Name cannot be empty");
        }
        if (name.length() < 2 || name.length() > 100) {
            throw new ValidationException("Name must be between 2 and 100 characters");
        }
    }
    
    public static void validateAge(Integer age) {
        if (age != null && (age < 0 || age > 120)) {
            throw new ValidationException("Age must be between 0 and 120");
        }
    }
    
    public static void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password cannot be empty");
        }
        if (password.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters long");
        }
    }
    
    public static void validateNumberOfPassengers(Integer numberOfPassengers) {
        if (numberOfPassengers == null || numberOfPassengers <= 0) {
            throw new ValidationException("Number of passengers must be greater than 0");
        }
    }
    
    public static void validateAvailableSeats(Integer availableSeats, Integer requestedSeats) {
        if (requestedSeats > availableSeats) {
            throw new ValidationException("Not enough available seats. Available: " + availableSeats);
        }
    }
}
