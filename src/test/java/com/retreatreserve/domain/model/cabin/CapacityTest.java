package com.retreatreserve.domain.model.cabin;

import com.retreatreserve.domain.exception.cabin.InvalidCapacityException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CapacityTest {

    @Test
    void shouldValidateAndCanAccommodate() {
        Capacity c = new Capacity(4);
        assertTrue(c.canAccommodate(3));
        assertFalse(c.canAccommodate(5));

        assertThrows(InvalidCapacityException.class, () -> new Capacity(0));
    }
}
