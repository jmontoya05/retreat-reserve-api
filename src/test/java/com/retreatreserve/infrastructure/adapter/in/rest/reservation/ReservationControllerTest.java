package com.retreatreserve.infrastructure.adapter.in.rest.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retreatreserve.application.command.reservation.CheckAvailabilityCommand;
import com.retreatreserve.application.command.reservation.CreateReservationCommand;
import com.retreatreserve.application.port.in.reservation.CancelReservationUseCase;
import com.retreatreserve.application.port.in.reservation.CheckAvailabilityUseCase;
import com.retreatreserve.application.port.in.reservation.CompleteReservationUseCase;
import com.retreatreserve.application.port.in.reservation.CreateReservationUseCase;
import com.retreatreserve.application.port.in.reservation.GetUserReservationsUseCase;
import com.retreatreserve.config.TestBaseConfig;
import com.retreatreserve.domain.model.reservation.DateRange;
import com.retreatreserve.domain.model.reservation.GuestDetails;
import com.retreatreserve.domain.model.reservation.Reservation;
import com.retreatreserve.domain.model.reservation.ReservationStatus;
import com.retreatreserve.infrastructure.adapter.in.rest.mapper.ReservationDtoMapper;
import com.retreatreserve.infrastructure.adapter.in.rest.reservation.dto.request.CreateReservationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
    classes = ReservationControllerTest.TestConfig.class,
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc(addFilters = false)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateReservationUseCase createReservationUseCase;

    @MockitoBean
    private CheckAvailabilityUseCase checkAvailabilityUseCase;

    @MockitoBean
    private CancelReservationUseCase cancelReservationUseCase;

    @MockitoBean
    private CompleteReservationUseCase completeReservationUseCase;

    @MockitoBean
    private GetUserReservationsUseCase getUserReservationsUseCase;

    @SpringBootConfiguration
    @EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        DataJpaRepositoriesAutoConfiguration.class
    })
    @Import({
        ReservationController.class,
        ReservationDtoMapper.class,
        TestBaseConfig.class
    })
    static class TestConfig {
    }

    @Test
    void shouldCreateReservationSuccessfully() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID cabinId = UUID.randomUUID();
        UUID reservationId = UUID.randomUUID();

        CreateReservationRequest request = new CreateReservationRequest(
            cabinId.toString(),
            LocalDate.now().plusDays(3),
            LocalDate.now().plusDays(7),
            4,
            "John Doe",
            "+1234567890",
            "Please arrive after 2pm"
        );

        Reservation reservation = new Reservation(
            reservationId,
            userId,
            cabinId,
            new DateRange(request.checkInDate(), request.checkOutDate()),
            new GuestDetails(request.numberOfGuests(), request.guestName(), request.guestPhone()),
            new BigDecimal("1600"),
            ReservationStatus.CONFIRMED,
            request.specialRequests(),
            true,
            LocalDate.now().atStartOfDay(),
            null
        );

        when(createReservationUseCase.execute(any(CreateReservationCommand.class)))
            .thenReturn(reservation);

        mockMvc.perform(post("/reservations")
                .contentType("application/json")
                .header("X-User-Id", userId.toString())
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(reservationId.toString()))
            .andExpect(jsonPath("$.cabinId").value(cabinId.toString()))
            .andExpect(jsonPath("$.numberOfGuests").value(4))
            .andExpect(jsonPath("$.totalPrice").value(1600));
    }

    @Test
    void shouldReturnBadRequestForInvalidReservationRequest() throws Exception {
        CreateReservationRequest invalidRequest = new CreateReservationRequest(
            "",
            LocalDate.now().minusDays(1),
            LocalDate.now().minusDays(1),
            0,
            "",
            "",
            null
        );

        mockMvc.perform(post("/reservations")
                .contentType("application/json")
                .header("X-User-Id", UUID.randomUUID().toString())
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCheckAvailabilitySuccessfully() throws Exception {
        UUID cabinId = UUID.randomUUID();
        LocalDate checkIn = LocalDate.now().plusDays(5);
        LocalDate checkOut = LocalDate.now().plusDays(8);

        when(checkAvailabilityUseCase.execute(any(CheckAvailabilityCommand.class))).thenReturn(true);

        mockMvc.perform(get("/reservations/check-availability")
                .param("cabinId", cabinId.toString())
                .param("checkInDate", checkIn.toString())
                .param("checkOutDate", checkOut.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.available").value(true));
    }
}
