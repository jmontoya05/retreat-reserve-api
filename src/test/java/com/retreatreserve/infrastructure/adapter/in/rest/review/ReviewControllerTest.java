package com.retreatreserve.infrastructure.adapter.in.rest.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retreatreserve.application.command.review.CreateReviewCommand;
import com.retreatreserve.application.port.in.review.CreateReviewUseCase;
import com.retreatreserve.application.port.in.review.GetCabinReviewsUseCase;
import com.retreatreserve.config.TestBaseConfig;
import com.retreatreserve.domain.model.reservation.Rating;
import com.retreatreserve.domain.model.reservation.Review;
import com.retreatreserve.infrastructure.adapter.in.rest.mapper.ReviewDtoMapper;
import com.retreatreserve.infrastructure.adapter.in.rest.review.dto.request.CreateReviewRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
    classes = ReviewControllerTest.TestConfig.class,
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc(addFilters = false)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateReviewUseCase createReviewUseCase;

    @MockitoBean
    private GetCabinReviewsUseCase getCabinReviewsUseCase;

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @Import({
        ReviewController.class,
        ReviewDtoMapper.class,
        TestBaseConfig.class
    })
    static class TestConfig {
    }

    @Test
    void shouldCreateReviewSuccessfully() throws Exception {
        UUID reviewId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID reservationId = UUID.randomUUID();
        UUID cabinId = UUID.randomUUID();

        CreateReviewRequest request = new CreateReviewRequest(
            reservationId.toString(), 5, "Wonderful stay"
        );

        Review review = new Review(
            reviewId,
            userId,
            cabinId,
            reservationId,
            new Rating(5),
            request.comment(),
            true,
            LocalDateTime.now(),
            null
        );

        when(createReviewUseCase.execute(any(CreateReviewCommand.class))).thenReturn(review);

        mockMvc.perform(post("/reviews")
                .contentType("application/json")
                .header("X-User-Id", userId.toString())
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(reviewId.toString()))
            .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void shouldReturnBadRequestForInvalidReviewRequest() throws Exception {
        CreateReviewRequest invalidRequest = new CreateReviewRequest("", 0, "");

        mockMvc.perform(post("/reviews")
                .contentType("application/json")
                .header("X-User-Id", UUID.randomUUID().toString())
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnCabinReviewsSuccessfully() throws Exception {
        UUID cabinId = UUID.randomUUID();
        Review review = new Review(
            UUID.randomUUID(),
            UUID.randomUUID(),
            cabinId,
            UUID.randomUUID(),
            new Rating(4),
            "Great cabin",
            true,
            LocalDateTime.now(),
            null
        );

        when(getCabinReviewsUseCase.execute(anyString())).thenReturn(List.of(review));

        mockMvc.perform(get("/reviews/cabin/" + cabinId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].cabinId").value(cabinId.toString()))
            .andExpect(jsonPath("$[0].rating").value(4));
    }
}
