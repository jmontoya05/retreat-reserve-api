package com.retreatreserve.application.service.reservation;

import com.retreatreserve.application.command.reservation.CreateReservationCommand;
import com.retreatreserve.application.port.out.notification.EmailService;
import com.retreatreserve.application.port.out.persistence.*;
import com.retreatreserve.domain.exception.cabin.*;
import com.retreatreserve.domain.exception.reservation.ExceedsCapacityException;
import com.retreatreserve.domain.exception.user.UserNotFoundException;
import com.retreatreserve.domain.model.cabin.*;
import com.retreatreserve.domain.model.iam.*;
import com.retreatreserve.domain.model.reservation.Reservation;
import com.retreatreserve.domain.service.AvailabilityService;
import com.retreatreserve.domain.service.PricingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateReservationServiceTest {
    
    @Mock
    private ReservationRepository reservationRepository;
    
    @Mock
    private CabinRepository cabinRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private AvailabilityService availabilityService;
    
    @Mock
    private PricingService pricingService;
    
    @Mock
    private EmailService emailService;
    
    @InjectMocks
    private CreateReservationService createReservationUseCase;
    
    @Test
    void shouldCreateReservationSuccessfully() {
        UUID userId = UUID.randomUUID();
        UUID cabinId = UUID.randomUUID();
        
        CreateReservationCommand command = new CreateReservationCommand(
            userId.toString(), cabinId.toString(),
            LocalDate.now().plusDays(1), LocalDate.now().plusDays(5),
            4, "John Doe", "+1234567890", null
        );
        
        User user = createTestUser();
        Cabin cabin = createTestCabin();
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cabinRepository.findById(cabinId)).thenReturn(Optional.of(cabin));
        when(pricingService.calculateTotalPrice(any(), any())).thenReturn(new BigDecimal("2000"));
        when(reservationRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        
        Reservation result = createReservationUseCase.execute(command);
        
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(cabinId, result.getCabinId());
        
        verify(availabilityService).ensureCabinAvailable(any(), any());
        verify(reservationRepository).save(any());
        verify(emailService).sendReservationConfirmation(any(), any(), any(), any(), any(), any());
    }
    
    @Test
    void shouldThrowExceptionWhenCabinNotAvailable() {
        UUID userId = UUID.randomUUID();
        UUID cabinId = UUID.randomUUID();
        
        CreateReservationCommand command = new CreateReservationCommand(
            userId.toString(), cabinId.toString(),
            LocalDate.now().plusDays(1), LocalDate.now().plusDays(5),
            4, "John Doe", "+1234567890", null
        );
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(createTestUser()));
        when(cabinRepository.findById(cabinId)).thenReturn(Optional.of(createTestCabin()));
        doThrow(new CabinNotAvailableException("Not available"))
            .when(availabilityService).ensureCabinAvailable(any(), any());
        
        assertThrows(CabinNotAvailableException.class, () -> createReservationUseCase.execute(command));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        UUID cabinId = UUID.randomUUID();

        CreateReservationCommand command = new CreateReservationCommand(
            userId.toString(), cabinId.toString(),
            LocalDate.now().plusDays(1), LocalDate.now().plusDays(5),
            4, "John Doe", "+1234567890", null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> createReservationUseCase.execute(command));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenGuestsExceedCapacity() {
        UUID userId = UUID.randomUUID();
        UUID cabinId = UUID.randomUUID();

        CreateReservationCommand command = new CreateReservationCommand(
            userId.toString(), cabinId.toString(),
            LocalDate.now().plusDays(1), LocalDate.now().plusDays(5),
            10, "John Doe", "+1234567890", null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(createTestUser()));
        when(cabinRepository.findById(cabinId)).thenReturn(Optional.of(createTestCabin()));

        assertThrows(ExceedsCapacityException.class, () -> createReservationUseCase.execute(command));
        verify(reservationRepository, never()).save(any());
    }
    
    private User createTestUser() {
        return new User(
            new FullName("John", "Doe"),
            new Email("john@example.com"),
            "hashed-password",
            "+1234567890"
        );
    }
    
    private Cabin createTestCabin() {
        Location location = new Location("City", "State", "Country", "Address", 
            BigDecimal.ZERO, BigDecimal.ZERO);
        Capacity capacity = new Capacity(6);
        
        return new Cabin("Test Cabin", "Description", UUID.randomUUID(),
            location, capacity, 3, 2, new BigDecimal("500"));
    }
}
