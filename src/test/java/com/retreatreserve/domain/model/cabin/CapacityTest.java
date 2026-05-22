package com.retreatreserve.domain.model.cabin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CapacityTest {

    @Test
    void shouldValidateAndCanAccommodate() {
        Capacity c = new Capacity(4);
        assertTrue(c.canAccommodate(3));
        assertFalse(c.canAccommodate(5));

        assertThrows(IllegalArgumentException.class, () -> new Capacity(0));
    }
}
