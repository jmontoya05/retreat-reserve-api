package com.retreatreserve.application.service.reservation;

import com.retreatreserve.application.port.in.reservation.CompleteReservationUseCase;
import com.retreatreserve.application.port.out.persistence.ReservationRepository;
import com.retreatreserve.domain.exception.reservation.ReservationNotFoundException;
import com.retreatreserve.domain.model.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompleteReservationService implements CompleteReservationUseCase {
    
    private final ReservationRepository reservationRepository;
    
    @Override
    @Transactional
    public Reservation execute(String reservationId) {
        log.info("Completing reservation: {}", reservationId);
        
        Reservation reservation = reservationRepository.findById(UUID.fromString(reservationId))
            .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));
        
        reservation.complete();
        
        return reservationRepository.save(reservation);
    }
}
