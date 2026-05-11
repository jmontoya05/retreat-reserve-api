package com.retreatreserve.application.service.reservation;

import com.retreatreserve.application.port.in.reservation.GetUserReservationsUseCase;
import com.retreatreserve.application.port.out.persistence.ReservationRepository;
import com.retreatreserve.domain.model.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserReservationsService implements GetUserReservationsUseCase {
    
    private final ReservationRepository reservationRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<Reservation> execute(String userId) {
        return reservationRepository.findByUserId(UUID.fromString(userId));
    }
}
