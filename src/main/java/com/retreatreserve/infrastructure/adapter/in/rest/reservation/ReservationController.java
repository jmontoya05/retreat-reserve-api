package com.retreatreserve.infrastructure.adapter.in.rest.reservation;

import com.retreatreserve.application.command.reservation.CheckAvailabilityCommand;
import com.retreatreserve.application.command.reservation.CreateReservationCommand;
import com.retreatreserve.application.port.in.reservation.CancelReservationUseCase;
import com.retreatreserve.application.port.in.reservation.CheckAvailabilityUseCase;
import com.retreatreserve.application.port.in.reservation.CompleteReservationUseCase;
import com.retreatreserve.application.port.in.reservation.CreateReservationUseCase;
import com.retreatreserve.application.port.in.reservation.GetUserReservationsUseCase;
import com.retreatreserve.domain.model.reservation.Reservation;
import com.retreatreserve.infrastructure.adapter.in.rest.reservation.dto.request.CreateReservationRequest;
import com.retreatreserve.infrastructure.adapter.in.rest.reservation.dto.response.ReservationResponse;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.retreatreserve.infrastructure.adapter.in.rest.mapper.ReservationDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservation Management", description = "Endpoints for creating, canceling, completing, and retrieving reservations, as well as checking availability")
public class ReservationController {
    
    private final CreateReservationUseCase createReservationUseCase;
    private final CheckAvailabilityUseCase checkAvailabilityUseCase;
    private final CancelReservationUseCase cancelReservationUseCase;
    private final CompleteReservationUseCase completeReservationUseCase;
    private final GetUserReservationsUseCase getUserReservationsUseCase;
    private final ReservationDtoMapper reservationDtoMapper;
    
    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody CreateReservationRequest request,
            @RequestHeader("X-User-Id") String userId) {
        
        CreateReservationCommand command = new CreateReservationCommand(
            userId, request.cabinId(),
            request.checkInDate(), request.checkOutDate(),
            request.numberOfGuests(), request.guestName(),
            request.guestPhone(), request.specialRequests()
        );
        
        Reservation reservation = createReservationUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationDtoMapper.toResponse(reservation));
    }
    
    @GetMapping("/check-availability")
    public ResponseEntity<Map<String, Boolean>> checkAvailability(
            @RequestParam String cabinId,
            @RequestParam LocalDate checkInDate,
            @RequestParam LocalDate checkOutDate) {
        
        CheckAvailabilityCommand command = new CheckAvailabilityCommand(cabinId, checkInDate, checkOutDate);
        boolean available = checkAvailabilityUseCase.execute(command);
        
        return ResponseEntity.ok(Map.of("available", available));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponse>> getUserReservations(@PathVariable String userId) {
        List<Reservation> reservations = getUserReservationsUseCase.execute(userId);
        return ResponseEntity.ok(
            reservations.stream()
                .map(reservationDtoMapper::toResponse)
                .toList()
        );
    }
    
    @PostMapping("/{reservationId}/cancel")
    public ResponseEntity<ReservationResponse> cancelReservation(@PathVariable String reservationId) {
        Reservation reservation = cancelReservationUseCase.execute(reservationId);
        return ResponseEntity.ok(reservationDtoMapper.toResponse(reservation));
    }
    
    @PostMapping("/{reservationId}/complete")
    public ResponseEntity<ReservationResponse> completeReservation(@PathVariable String reservationId) {
        Reservation reservation = completeReservationUseCase.execute(reservationId);
        return ResponseEntity.ok(reservationDtoMapper.toResponse(reservation));
    }
}
