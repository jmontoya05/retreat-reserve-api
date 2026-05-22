package com.retreatreserve.domain.model.cabin;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FeatureTest {

    @Test
    void activateAndDeactivateAndUpdate() {
        Feature feature = new Feature(
            UUID.randomUUID(),
            "Test",
            "icon.png",
            "desc",
            true,
            LocalDateTime.now(),
            null
        );

        feature.deactivate();
        assertFalse(feature.getActive());

        feature.activate();
        assertTrue(feature.getActive());

        feature.updateDetails("NewName","newicon.png","newdesc");
        assertEquals("NewName", feature.getName());
        assertEquals("newicon.png", feature.getIconKey());
    }
}
