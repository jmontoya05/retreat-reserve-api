package com.retreatreserve.application.service.cabin;

import com.retreatreserve.application.port.in.cabin.GetCabinDetailsUseCase;
import com.retreatreserve.application.port.out.persistence.CabinRepository;
import com.retreatreserve.domain.exception.cabin.CabinNotFoundException;
import com.retreatreserve.domain.model.cabin.Cabin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCabinDetailsService implements GetCabinDetailsUseCase {
    
    private final CabinRepository cabinRepository;
    
    @Transactional(readOnly = true)
    public Cabin execute(String cabinId) {
        return cabinRepository.findById(UUID.fromString(cabinId))
            .orElseThrow(() -> new CabinNotFoundException("Cabin not found: " + cabinId));
    }
}
