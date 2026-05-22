package com.retreatreserve.domain.service;

import com.retreatreserve.domain.model.cabin.*;
import com.retreatreserve.domain.model.reservation.DateRange;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PricingServiceTest {
    
    private final PricingService pricingService = new PricingService();
    
    @Test
    void shouldCalculateTotalPrice() {
        Cabin cabin = createCabinWithPrice(new BigDecimal("500"));
        DateRange dateRange = new DateRange(
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(6)  // 5 nights
        );
        
        BigDecimal totalPrice = pricingService.calculateTotalPrice(cabin, dateRange);
        
        assertEquals(new BigDecimal("2500"), totalPrice);  // 500 * 5
    }
    
    @Test
    void shouldCalculateForOneNight() {
        Cabin cabin = createCabinWithPrice(new BigDecimal("1000"));
        DateRange dateRange = new DateRange(
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(2)  // 1 night
        );
        
        BigDecimal totalPrice = pricingService.calculateTotalPrice(cabin, dateRange);
        
        assertEquals(new BigDecimal("1000"), totalPrice);
    }
    
    private Cabin createCabinWithPrice(BigDecimal pricePerNight) {
        Location location = new Location("City", "State", "Country", "Address",
            BigDecimal.ZERO, BigDecimal.ZERO);
        Capacity capacity = new Capacity(6);
        
        return new Cabin("Cabin", "Description", UUID.randomUUID(),
            location, capacity, 3, 2, pricePerNight);
    }
}
