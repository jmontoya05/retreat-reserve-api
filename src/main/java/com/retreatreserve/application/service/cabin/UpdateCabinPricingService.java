package com.retreatreserve.application.service.cabin;

import com.retreatreserve.application.port.in.cabin.UpdateCabinPricingUseCase;
import com.retreatreserve.application.port.out.persistence.CabinRepository;
import com.retreatreserve.domain.exception.cabin.CabinNotFoundException;
import com.retreatreserve.domain.model.cabin.Cabin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateCabinPricingService implements UpdateCabinPricingUseCase {
    
    private final CabinRepository cabinRepository;
    
    @Override
    @Transactional
    public Cabin execute(String cabinId, BigDecimal newPrice) {
        log.info("Updating pricing for cabin: {} to {}", cabinId, newPrice);
        
        Cabin cabin = cabinRepository.findById(UUID.fromString(cabinId))
            .orElseThrow(() -> new CabinNotFoundException("Cabin not found"));
        
        cabin.updatePricing(newPrice);
        
        return cabinRepository.save(cabin);
    }
}
