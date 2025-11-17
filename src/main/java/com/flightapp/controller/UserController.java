package com.flightapp.controller;

import com.flightapp.dto.UserRegistrationDTO;
import com.flightapp.entity.User;
import com.flightapp.exception.ValidationException;
import com.flightapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1.0/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {
    
    private final UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserRegistrationDTO dto) {
        log.info("Registering user with email: {}", dto.getEmail());
        
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email is required");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new ValidationException("Password is required");
        }
        if (dto.getFirstName() == null || dto.getFirstName().trim().isEmpty()) {
            throw new ValidationException("First name is required");
        }
        if (dto.getLastName() == null || dto.getLastName().trim().isEmpty()) {
            throw new ValidationException("Last name is required");
        }
        
        User user = User.builder()
            .email(dto.getEmail())
            .password(dto.getPassword())
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .phoneNumber(dto.getPhoneNumber())
            .aadharNumber(dto.getAadharNumber())
            .address(dto.getAddress())
            .city(dto.getCity())
            .state(dto.getState())
            .pincode(dto.getPincode())
            .build();
        
        User savedUser = userService.registerUser(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", savedUser.getUserId());
        response.put("email", savedUser.getEmail());
        response.put("firstName", savedUser.getFirstName());
        response.put("lastName", savedUser.getLastName());
        response.put("message", "User registered successfully");
        
        log.info("User registered successfully with ID: {}", savedUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        log.info("Fetching user with ID: {}", userId);
        
        if (userId == null || userId <= 0) {
            throw new ValidationException("Invalid user ID");
        }
        
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
}
