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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCabinDetailsServiceTest {

    @Mock
    private CabinRepository cabinRepository;

    @InjectMocks
    private GetCabinDetailsService cabinDetailsService;

    @Test
    void shouldReturnCabinWhenFound() {
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

        Cabin result = cabinDetailsService.execute(cabinId.toString());

        assertEquals(cabin, result);
    }

    @Test
    void shouldThrowWhenCabinNotFound() {
        UUID cabinId = UUID.randomUUID();
        String cabinIdStr = cabinId.toString();

        when(cabinRepository.findById(cabinId)).thenReturn(Optional.empty());

        assertThrows(CabinNotFoundException.class, () -> cabinDetailsService.execute(cabinIdStr));
    }
}
