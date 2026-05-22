package com.retreatreserve.application.service.cabin;

import com.retreatreserve.application.port.out.persistence.CabinRepository;
import com.retreatreserve.domain.exception.cabin.CabinNotFoundException;
import com.retreatreserve.domain.model.cabin.Cabin;
import com.retreatreserve.domain.model.cabin.Capacity;
import com.retreatreserve.domain.model.cabin.Location;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCabinPricingServiceTest {

    @Mock
    private CabinRepository cabinRepository;

    @InjectMocks
    private UpdateCabinPricingService updateCabinPricingService;

    @Test
    void shouldUpdateCabinPriceWhenFound() {
        UUID cabinId = UUID.randomUUID();
        Cabin cabin = new Cabin(
            cabinId,
            "Retreat Cabin",
            "Beautiful cabin",
            UUID.randomUUID(),
            new Location("City", "State", "Country", "Address", BigDecimal.ZERO, BigDecimal.ZERO),
            new Capacity(4),
            2,
            1,
            new BigDecimal("200"),
            BigDecimal.ZERO,
            0,
            null,
            true,
            List.of(),
            List.of(),
            List.of(),
            null,
            null
        );

        when(cabinRepository.findById(cabinId)).thenReturn(Optional.of(cabin));
        when(cabinRepository.save(any(Cabin.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cabin updated = updateCabinPricingService.execute(cabinId.toString(), new BigDecimal("250"));

        assertEquals(new BigDecimal("250"), updated.getPricePerNight());
    }

    @Test
    void shouldThrowWhenCabinNotFound() {
        UUID cabinId = UUID.randomUUID();
        String cabinIdStr = cabinId.toString();
        BigDecimal newPrice = new BigDecimal("250");

        when(cabinRepository.findById(cabinId)).thenReturn(Optional.empty());

        assertThrows(CabinNotFoundException.class, () -> updateCabinPricingService.execute(cabinIdStr, newPrice));
    }
}
