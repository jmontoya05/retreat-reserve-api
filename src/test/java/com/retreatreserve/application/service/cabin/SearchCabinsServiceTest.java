package com.retreatreserve.application.service.cabin;

import com.retreatreserve.application.command.cabin.SearchCabinsCommand;
import com.retreatreserve.application.port.out.persistence.CabinRepository;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchCabinsServiceTest {

    @Mock
    private CabinRepository cabinRepository;

    @InjectMocks
    private SearchCabinsService searchCabinsService;

    @Test
    void shouldSearchAvailableCabinsWhenNoCategoryProvided() {
        Cabin cabin = createCabin("Mountain Escape");
        List<Cabin> expected = List.of(cabin);
        SearchCabinsCommand command = new SearchCabinsCommand(
            "Mountain",
            4,
            new BigDecimal("300"),
            null);

        when(cabinRepository.findAvailableCabins("Mountain", 4, new BigDecimal("300")))
            .thenReturn(expected);

        List<Cabin> result = searchCabinsService.execute(command);

        assertEquals(expected, result);
        verify(cabinRepository).findAvailableCabins("Mountain", 4, new BigDecimal("300"));
        verifyNoMoreInteractions(cabinRepository);
    }

    @Test
    void shouldSearchByCategoryWhenCategoryProvided() {
        UUID categoryId = UUID.randomUUID();
        Cabin cabin = createCabin("Lakeside Cabin");
        List<Cabin> expected = List.of(cabin);
        SearchCabinsCommand command = new SearchCabinsCommand(
            null,
            null,
            null,
            categoryId.toString()
        );

        when(cabinRepository.findByCategoryId(categoryId)).thenReturn(expected);

        List<Cabin> result = searchCabinsService.execute(command);

        assertEquals(expected, result);
        verify(cabinRepository).findByCategoryId(categoryId);
        verifyNoMoreInteractions(cabinRepository);
    }

    private Cabin createCabin(String name) {
        Location location = new Location(
            "City",
            "State",
            "Country",
            "Address",
            BigDecimal.ZERO,
            BigDecimal.ZERO
        );
        Capacity capacity = new Capacity(4);
        return new Cabin(
            name, 
            "A lovely escape",
            UUID.randomUUID(),
            location,
            capacity,
            2,
            1,
            new BigDecimal("150")
        );
    }
}
