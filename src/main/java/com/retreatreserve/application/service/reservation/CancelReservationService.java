package com.retreatreserve.application.service.reservation;

import com.retreatreserve.application.port.in.reservation.CancelReservationUseCase;
import com.retreatreserve.application.port.out.notification.EmailService;
import com.retreatreserve.application.port.out.persistence.CabinRepository;
import com.retreatreserve.application.port.out.persistence.ReservationRepository;
import com.retreatreserve.application.port.out.persistence.UserRepository;
import com.retreatreserve.domain.exception.reservation.ReservationNotFoundException;
import com.retreatreserve.domain.model.cabin.Cabin;
import com.retreatreserve.domain.model.iam.User;
import com.retreatreserve.domain.model.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CancelReservationService implements CancelReservationUseCase {
    
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CabinRepository cabinRepository;
    private final EmailService emailService;
    
    @Override
    @Transactional
    public Reservation execute(String reservationId) {
        log.info("Canceling reservation: {}", reservationId);
        
        Reservation reservation = reservationRepository.findById(UUID.fromString(reservationId))
            .orElseThrow(() -> new ReservationNotFoundException("Reservation not found: " + reservationId));
        
        reservation.cancel();
        
        Reservation cancelled = reservationRepository.save(reservation);
        
        try {
            User user = userRepository.findById(reservation.getUserId()).orElse(null);
            Cabin cabin = cabinRepository.findById(reservation.getCabinId()).orElse(null);
            
            if (user != null && cabin != null) {
                emailService.sendReservationCancellation(
                    user.getEmail().getValue(),
                    user.getFullName().getFullName(),
                    cabin.getName()
                );
            }
        } catch (Exception e) {
            log.error("Failed to send cancellation email", e);
        }
        
        log.info("Reservation cancelled: {}", cancelled.getId());
        return cancelled;
    }
}
