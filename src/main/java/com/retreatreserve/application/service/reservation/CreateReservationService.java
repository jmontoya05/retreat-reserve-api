package com.retreatreserve.application.service.reservation;

import com.retreatreserve.application.command.reservation.CreateReservationCommand;
import com.retreatreserve.application.port.in.reservation.CreateReservationUseCase;
import com.retreatreserve.application.port.out.notification.EmailService;
import com.retreatreserve.application.port.out.persistence.CabinRepository;
import com.retreatreserve.application.port.out.persistence.ReservationRepository;
import com.retreatreserve.application.port.out.persistence.UserRepository;
import com.retreatreserve.domain.exception.cabin.CabinNotFoundException;
import com.retreatreserve.domain.exception.cabin.CabinNotAvailableException;
import com.retreatreserve.domain.exception.reservation.ExceedsCapacityException;
import com.retreatreserve.domain.exception.user.UserNotFoundException;
import com.retreatreserve.domain.model.cabin.Cabin;
import com.retreatreserve.domain.model.iam.User;
import com.retreatreserve.domain.model.reservation.DateRange;
import com.retreatreserve.domain.model.reservation.GuestDetails;
import com.retreatreserve.domain.model.reservation.Reservation;
import com.retreatreserve.domain.service.AvailabilityService;
import com.retreatreserve.domain.service.PricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Create Reservation with Availability Check.
 * This prevents double bookings - absolutely essential!
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateReservationService implements CreateReservationUseCase {
    
    private final ReservationRepository reservationRepository;
    private final CabinRepository cabinRepository;
    private final UserRepository userRepository;
    private final AvailabilityService availabilityService;
    private final PricingService pricingService;
    private final EmailService emailService;
    
    @Override
    @Transactional
    public Reservation execute(CreateReservationCommand command) {
        log.info("Creating reservation for cabin: {} by user: {}", command.cabinId(), command.userId());
        
        UUID userId = UUID.fromString(command.userId());
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found: " + command.userId()));
        
        UUID cabinId = UUID.fromString(command.cabinId());
        Cabin cabin = cabinRepository.findById(cabinId)
            .orElseThrow(() -> new CabinNotFoundException("Cabin not found: " + command.cabinId()));
        
        if (!cabin.isAvailableForBooking()) {
            throw new CabinNotAvailableException("Cabin is not available for booking. Status: " + cabin.getStatus());
        }
        
        DateRange dateRange = new DateRange(command.checkInDate(), command.checkOutDate());
        
        availabilityService.ensureCabinAvailable(cabinId, dateRange);
        
        if (!cabin.getCapacity().canAccommodate(command.numberOfGuests())) {
            throw new ExceedsCapacityException(
                "Number of guests (" + command.numberOfGuests() + 
                ") exceeds cabin capacity (" + cabin.getCapacity().getMaxGuests() + ")"
            );
        }
        
        BigDecimal totalPrice = pricingService.calculateTotalPrice(cabin, dateRange);
        
        GuestDetails guestDetails = new GuestDetails(
            command.numberOfGuests(),
            command.guestName(),
            command.guestPhone()
        );
        
        Reservation reservation = new Reservation(
            userId,
            cabinId,
            dateRange,
            guestDetails,
            totalPrice
        );
        
        if (command.specialRequests() != null && !command.specialRequests().isBlank()) {
            reservation.updateSpecialRequests(command.specialRequests());
        }
        
        Reservation savedReservation = reservationRepository.save(reservation);
        
        try {
            emailService.sendReservationConfirmation(
                user.getEmail().getValue(),
                user.getFullName().getFullName(),
                cabin.getName(),
                dateRange.getCheckInDate().toString(),
                dateRange.getCheckOutDate().toString(),
                totalPrice.toString()
            );
            log.info("Reservation confirmation email sent to: {}", user.getEmail().getValue());
        } catch (Exception e) {
            log.error("Failed to send confirmation email", e);
        }
        
        log.info("Reservation created successfully: {}", savedReservation.getId());
        return savedReservation;
    }
}
