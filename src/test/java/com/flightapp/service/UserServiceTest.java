package com.flightapp.service;

import com.flightapp.entity.User;
import com.flightapp.repository.UserRepository;
import com.flightapp.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    private User testUser;
    
    @BeforeEach
    public void setUp() {
        testUser = User.builder()
            .userId(1)
            .email("user@example.com")
            .password("password123")
            .firstName("John")
            .lastName("Doe")
            .phoneNumber("9876543210")
            .role("USER")
            .isActive(true)
            .build();
    }
    
    @Test
    void testRegisterUser_Success() {
        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        User result = userService.registerUser(testUser);
        
        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
        assertEquals("USER", result.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    void testRegisterUser_EmailAlreadyExists() {
        when(userRepository.existsByEmail("user@example.com")).thenReturn(true);
        
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(testUser));
    }
    
    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        
        User result = userService.getUserById(1);
        
        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
    }
    
    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1));
    }
    
    @Test
    void testGetUserByEmail_Success() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(testUser));
        
        User result = userService.getUserByEmail("user@example.com");
        
        assertNotNull(result);
        assertEquals(1, result.getUserId());
    }
    
    @Test
    void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByEmail("invalid@example.com"));
    }
}
