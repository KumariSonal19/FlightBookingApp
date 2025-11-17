package com.flightapp.service;

import com.flightapp.entity.User;
import com.flightapp.repository.UserRepository;
import com.flightapp.exception.ResourceNotFoundException;
import com.flightapp.validation.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    
    public User registerUser(User user) {
        log.info("Registering user with email: {}", user.getEmail());
        
        ValidationUtils.validateEmail(user.getEmail());
        ValidationUtils.validatePassword(user.getPassword());
        ValidationUtils.validateName(user.getFirstName());
        ValidationUtils.validateName(user.getLastName());
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        
        user.setRole("USER");
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getUserId());
        return savedUser;
    }
    
    public User getUserById(Integer userId) {
        log.info("Fetching user with ID: {}", userId);
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }
    
    public User getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        ValidationUtils.validateEmail(email);
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
