package com.retreatreserve.domain.service;

import com.retreatreserve.application.port.out.persistence.ReservationRepository;
import com.retreatreserve.domain.exception.cabin.CabinNotAvailableException;
import com.retreatreserve.domain.model.reservation.DateRange;
import com.retreatreserve.domain.model.reservation.Reservation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceTest {
    
    @Mock
    private ReservationRepository reservationRepository;
    
    @InjectMocks
    private AvailabilityService availabilityService;
    
    @Test
    void shouldConfirmCabinIsAvailable() {
        UUID cabinId = UUID.randomUUID();
        DateRange dateRange = new DateRange(
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(5)
        );
        
        when(reservationRepository.findOverlappingReservations(any(), any(), any()))
            .thenReturn(Collections.emptyList());
        
        assertDoesNotThrow(() -> availabilityService.ensureCabinAvailable(cabinId, dateRange));
    }
    
    @Test
    void shouldThrowExceptionWhenCabinNotAvailable() {
        UUID cabinId = UUID.randomUUID();
        DateRange dateRange = new DateRange(
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(5)
        );
        
        Reservation overlapping = mock(Reservation.class);
        when(reservationRepository.findOverlappingReservations(any(), any(), any()))
            .thenReturn(List.of(overlapping));
        
        assertThrows(CabinNotAvailableException.class, 
            () -> availabilityService.ensureCabinAvailable(cabinId, dateRange));
    }
    
    @Test
    void shouldCheckAvailability() {
        UUID cabinId = UUID.randomUUID();
        
        when(reservationRepository.findOverlappingReservations(any(), any(), any()))
            .thenReturn(Collections.emptyList());
        
        boolean available = availabilityService.isAvailable(
            cabinId,
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(5)
        );
        
        assertTrue(available);
    }
}
