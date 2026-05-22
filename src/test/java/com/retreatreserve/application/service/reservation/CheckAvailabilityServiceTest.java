package com.retreatreserve.application.service.reservation;

import com.retreatreserve.application.command.reservation.CheckAvailabilityCommand;
import com.retreatreserve.domain.service.AvailabilityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckAvailabilityServiceTest {

    @Mock
    private AvailabilityService availabilityService;

    @InjectMocks
    private CheckAvailabilityService checkAvailabilityService;

    @Test
    void shouldReturnTrueWhenCabinIsAvailable() {
        UUID cabinId = UUID.randomUUID();
        CheckAvailabilityCommand command = new CheckAvailabilityCommand(
            cabinId.toString(),
            LocalDate.now().plusDays(10),
            LocalDate.now().plusDays(15)
        );

        when(availabilityService.isAvailable(cabinId, command.checkInDate(), command.checkOutDate()))
            .thenReturn(true);

        assertTrue(checkAvailabilityService.execute(command));
    }

    @Test
    void shouldReturnFalseWhenCabinIsUnavailable() {
        UUID cabinId = UUID.randomUUID();
        CheckAvailabilityCommand command = new CheckAvailabilityCommand(
            cabinId.toString(),
            LocalDate.now().plusDays(10),
            LocalDate.now().plusDays(15)
        );

        when(availabilityService.isAvailable(cabinId, command.checkInDate(), command.checkOutDate()))
            .thenReturn(false);

        assertFalse(checkAvailabilityService.execute(command));
    }
}
