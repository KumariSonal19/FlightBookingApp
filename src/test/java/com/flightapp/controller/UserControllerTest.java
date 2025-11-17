package com.flightapp.controller;

import com.flightapp.dto.UserRegistrationDTO;
import com.flightapp.entity.User;
import com.flightapp.exception.ResourceNotFoundException;
import com.flightapp.exception.ValidationException;
import com.flightapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private UserController userController;
    
    private UserRegistrationDTO registrationDTO;
    private User testUser;
    
    @BeforeEach
    public void setUp() {
        registrationDTO = new UserRegistrationDTO();
        registrationDTO.setEmail("john@example.com");
        registrationDTO.setPassword("password123");
        registrationDTO.setFirstName("John");
        registrationDTO.setLastName("Doe");
        registrationDTO.setPhoneNumber("9876543210");
        
        testUser = User.builder()
            .userId(1)
            .email("john@example.com")
            .firstName("John")
            .lastName("Doe")
            .phoneNumber("9876543210")
            .role("USER")
            .isActive(true)
            .build();
    }
    
    @Test
    void testRegisterUser_Success() {
        when(userService.registerUser(any(User.class)))
            .thenReturn(testUser);
        
        ResponseEntity<Map<String, Object>> response = userController.registerUser(registrationDTO);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().get("userId"));
        assertEquals("john@example.com", response.getBody().get("email"));
        assertTrue(response.getBody().containsKey("message"));
        verify(userService, times(1)).registerUser(any());
    }
    
    @Test
    void testRegisterUser_NullEmail() {
        registrationDTO.setEmail(null);
        
        assertThrows(ValidationException.class, () -> userController.registerUser(registrationDTO));
    }
    
    @Test
    void testRegisterUser_EmptyEmail() {
        registrationDTO.setEmail("");
        
        assertThrows(ValidationException.class, () -> userController.registerUser(registrationDTO));
    }
    
    @Test
    void testRegisterUser_EmptyPassword() {
        registrationDTO.setPassword("");
        
        assertThrows(ValidationException.class, () -> userController.registerUser(registrationDTO));
    }
    
    @Test
    void testRegisterUser_InvalidInput_NullFirstName() {
        registrationDTO.setFirstName(null);
        
        assertThrows(ValidationException.class, () -> userController.registerUser(registrationDTO));
    }
    
    @Test
    void testRegisterUser_EmptyFirstName() {
        registrationDTO.setFirstName("");
        
        assertThrows(ValidationException.class, () -> userController.registerUser(registrationDTO));
    }
    
    @Test
    void testRegisterUser_InvalidInput_NullLastName() {
        registrationDTO.setLastName(null);
        
        assertThrows(ValidationException.class, () -> userController.registerUser(registrationDTO));
    }
    
    @Test
    void testRegisterUser_EmptyLastName() {
        registrationDTO.setLastName("");
        
        assertThrows(ValidationException.class, () -> userController.registerUser(registrationDTO));
    }
    
    @Test
    void testGetUserById_Success() {
        when(userService.getUserById(1))
            .thenReturn(testUser);
        
        ResponseEntity<User> response = userController.getUserById(1);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getUserId());
        assertEquals("john@example.com", response.getBody().getEmail());
        assertTrue(response.getBody().isActive());
    }
    
    @Test
    void testGetUserById_NotFound() {
        when(userService.getUserById(999))
            .thenThrow(new ResourceNotFoundException("User not found"));
        
        assertThrows(ResourceNotFoundException.class, () -> userController.getUserById(999));
    }
    
    @Test
    void testGetUserById_InvalidId() {
        assertThrows(ValidationException.class, () -> userController.getUserById(-1));
    }
    
    @Test
    void testGetUserById_InvalidId_Zero() {
        assertThrows(ValidationException.class, () -> userController.getUserById(0));
    }
    
    @Test
    void testRegisterUser_DuplicateEmail() {
        when(userService.registerUser(any(User.class)))
            .thenThrow(new ValidationException("Email already exists"));
        
        assertThrows(ValidationException.class, () -> userController.registerUser(registrationDTO));
    }
    
    @Test
    void testGetUserById_LargeId() {
        when(userService.getUserById(999999))
            .thenThrow(new ResourceNotFoundException("User not found"));
        
        assertThrows(ResourceNotFoundException.class, () -> userController.getUserById(999999));
    }
    
    @Test
    void testRegisterUser_SuccessAlternative() {
        User alternativeUser = User.builder()
            .userId(2)
            .email("jane@example.com")
            .firstName("Jane")
            .lastName("Smith")
            .phoneNumber("9876543211")
            .role("USER")
            .isActive(true)
            .build();
        
        UserRegistrationDTO alternativeDTO = new UserRegistrationDTO();
        alternativeDTO.setEmail("jane@example.com");
        alternativeDTO.setPassword("securepass456");
        alternativeDTO.setFirstName("Jane");
        alternativeDTO.setLastName("Smith");
        alternativeDTO.setPhoneNumber("9876543211");
        
        when(userService.registerUser(any(User.class)))
            .thenReturn(alternativeUser);
        
        ResponseEntity<Map<String, Object>> response = userController.registerUser(alternativeDTO);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(2, response.getBody().get("userId"));
        assertEquals("jane@example.com", response.getBody().get("email"));

    }
}
    
  