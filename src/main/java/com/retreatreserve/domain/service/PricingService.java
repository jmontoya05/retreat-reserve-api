package com.retreatreserve.domain.service;

import com.retreatreserve.domain.model.cabin.Cabin;
import com.retreatreserve.domain.model.reservation.DateRange;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Domain service for calculating reservation pricing.
 */
@Service
public class PricingService {
    
    /**
     * Calculates total price for a reservation.
     */
    public BigDecimal calculateTotalPrice(Cabin cabin, DateRange dateRange) {
        long numberOfNights = dateRange.getNumberOfNights();
        return cabin.getPricePerNight().multiply(BigDecimal.valueOf(numberOfNights));
    }
}
