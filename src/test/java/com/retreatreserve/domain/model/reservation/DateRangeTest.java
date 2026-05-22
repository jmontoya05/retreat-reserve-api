package com.retreatreserve.domain.model.reservation;

import com.retreatreserve.domain.exception.reservation.InvalidDateRangeException;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class DateRangeTest {
    
    @Test
    void shouldCreateValidDateRange() {
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(5);
        
        DateRange dateRange = new DateRange(checkIn, checkOut);
        
        assertEquals(checkIn, dateRange.getCheckInDate());
        assertEquals(checkOut, dateRange.getCheckOutDate());
        assertEquals(4, dateRange.getNumberOfNights());
    }
    
    @Test
    void shouldThrowExceptionForInvalidDateRange() {
        LocalDate checkIn = LocalDate.now().plusDays(5);
        LocalDate checkOut = LocalDate.now().plusDays(1);
        
        assertThrows(InvalidDateRangeException.class, () -> new DateRange(checkIn, checkOut));
    }
    
    @Test
    void shouldThrowExceptionForPastDates() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        LocalDate futureDate = LocalDate.now().plusDays(5);
        
        assertThrows(InvalidDateRangeException.class, () -> new DateRange(pastDate, futureDate));
    }
}
