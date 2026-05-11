package com.retreatreserve.application.service.reservation;

import com.retreatreserve.application.command.reservation.CheckAvailabilityCommand;
import com.retreatreserve.application.port.in.reservation.CheckAvailabilityUseCase;
import com.retreatreserve.domain.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckAvailabilityService implements CheckAvailabilityUseCase{
    
    private final AvailabilityService availabilityService;
    
    @Override
    @Transactional(readOnly = true)
    public boolean execute(CheckAvailabilityCommand command) {
        UUID cabinId = UUID.fromString(command.cabinId());
        
        return availabilityService.isAvailable(
            cabinId,
            command.checkInDate(),
            command.checkOutDate()
        );
    }
}
