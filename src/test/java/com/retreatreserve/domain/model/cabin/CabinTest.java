package com.retreatreserve.domain.model.cabin;

import com.retreatreserve.domain.exception.cabin.DuplicateFeatureException;
import com.retreatreserve.domain.exception.cabin.InvalidPriceException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CabinTest {

    @Test
    void shouldPreventAddingDuplicateFeature() {
        Cabin cabin = new Cabin(
            "Test Cabin",
            "Desc",
            UUID.randomUUID(),
            new Location("City", "State", "Country"),
            new Capacity(4),
            2,
            1,
            new BigDecimal("120")
        );

        UUID featureId = UUID.randomUUID();
        cabin.addFeature(featureId);

        assertThrows(DuplicateFeatureException.class, () -> cabin.addFeature(featureId));
    }

    @Test
    void shouldRejectInvalidPrice() {
        Cabin cabin = new Cabin(
            "Test Cabin",
            "Desc",
            UUID.randomUUID(),
            new Location("City", "State", "Country"),
            new Capacity(4),
            2,
            1,
            new BigDecimal("120")
        );
        BigDecimal invalidPrice = new BigDecimal("0");
        BigDecimal negativePrice = new BigDecimal("-10");

        assertThrows(InvalidPriceException.class, () -> cabin.setPricePerNight(invalidPrice));
        assertThrows(InvalidPriceException.class, () -> cabin.setPricePerNight(negativePrice));
    }

    @Test
    void shouldManageImagesAndPrimaryFlag() {
        Cabin cabin = new Cabin(
            "Image Cabin",
            "Desc",
            UUID.randomUUID(),
            new Location("City", "State", "Country"),
            new Capacity(2),
            1,
            1,
            new BigDecimal("80")
        );

        CabinImage img1 = new CabinImage(UUID.randomUUID(), "k1", 1, false, LocalDateTime.now());
        CabinImage img2 = new CabinImage(UUID.randomUUID(), "k2", 2, false, LocalDateTime.now());

        cabin.addImage(img1);
        cabin.addImage(img2);

        assertTrue(cabin.hasRequiredImages());

        cabin.setPrimaryImage(img2.getId());

        boolean primaryFound = cabin.getImages().stream().anyMatch(CabinImage::getIsPrimary);
        assertTrue(primaryFound);
    }
}
