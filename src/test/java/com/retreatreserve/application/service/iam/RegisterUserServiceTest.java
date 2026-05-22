package com.retreatreserve.application.service.iam;

import com.retreatreserve.application.command.iam.RegisterUserCommand;
import com.retreatreserve.application.port.out.notification.EmailService;
import com.retreatreserve.application.port.out.persistence.UserRepository;
import com.retreatreserve.application.port.out.security.PasswordEncoder;
import com.retreatreserve.domain.exception.user.EmailAlreadyExistsException;
import com.retreatreserve.domain.model.iam.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private EmailService emailService;
    
    @InjectMocks
    private RegisterUserService registerUserService;
    
    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterUserCommand command = new RegisterUserCommand(
            "John", "Doe", "john@example.com", "password123", "+1234567890"
        );
        
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hashed-password");
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        
        User result = registerUserService.execute(command);
        
        assertNotNull(result);
        assertEquals("John", result.getFullName().getFirstName());
        assertEquals("Doe", result.getFullName().getLastName());
        
        verify(userRepository).save(any(User.class));
        verify(emailService).sendVerificationEmail(any(), any(), any());
    }
    
    @Test
    void shouldThrowExceptionWhenEmailExists() {
        RegisterUserCommand command = new RegisterUserCommand(
            "John", "Doe", "existing@example.com", "password123", "+1234567890"
        );
        
        when(userRepository.existsByEmail(any())).thenReturn(true);
        
        assertThrows(EmailAlreadyExistsException.class, () -> registerUserService.execute(command));
        verify(userRepository, never()).save(any());
    }
}