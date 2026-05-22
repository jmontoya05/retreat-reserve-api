package com.retreatreserve.infrastructure.adapter.in.rest.cabin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retreatreserve.application.command.cabin.CreateCabinCommand;
import com.retreatreserve.application.command.cabin.SearchCabinsCommand;
import com.retreatreserve.application.port.in.cabin.CreateCabinUseCase;
import com.retreatreserve.application.port.in.cabin.GetCabinDetailsUseCase;
import com.retreatreserve.application.port.in.cabin.SearchCabinsUseCase;
import com.retreatreserve.application.port.in.cabin.UpdateCabinPricingUseCase;
import com.retreatreserve.application.port.in.storage.GeneratePreSignedUrlUseCase;
import com.retreatreserve.config.TestBaseConfig;
import com.retreatreserve.domain.model.cabin.Cabin;
import com.retreatreserve.domain.model.cabin.CabinImage;
import com.retreatreserve.domain.model.cabin.CabinStatus;
import com.retreatreserve.domain.model.cabin.Capacity;
import com.retreatreserve.domain.model.cabin.Location;
import com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.request.CreateCabinRequest;
import com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.request.UpdatePricingRequest;
import com.retreatreserve.infrastructure.adapter.in.rest.mapper.CabinDtoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
    classes = CabinControllerTest.TestConfig.class,
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc(addFilters = false)
class CabinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateCabinUseCase createCabinUseCase;

    @MockitoBean
    private SearchCabinsUseCase searchCabinsUseCase;

    @MockitoBean
    private GetCabinDetailsUseCase getCabinDetailsUseCase;

    @MockitoBean
    private UpdateCabinPricingUseCase updateCabinPricingUseCase;

    @MockitoBean
    private GeneratePreSignedUrlUseCase generatePreSignedUrlUseCase;

    @SpringBootConfiguration
    @EnableAutoConfiguration(exclude = {
        SecurityAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        DataJpaRepositoriesAutoConfiguration.class
    })
    @Import({
        CabinController.class,
        CabinDtoMapper.class,
        TestBaseConfig.class
    })
    static class TestConfig {
    }

    @Test
    void shouldCreateCabinSuccessfully() throws Exception {
        UUID cabinId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID featureId = UUID.randomUUID();

        CreateCabinRequest request = new CreateCabinRequest(
            "Lake Cabin",
            "Cozy cabin by the lake",
            categoryId.toString(),
            "Lakeside",
            "State",
            "Country",
            "123 Water St",
            new BigDecimal("45.123"),
            new BigDecimal("-71.321"),
            4,
            2,
            1,
            new BigDecimal("250"),
            List.of("image-key"),
            List.of(featureId.toString()),
            null
        );

        Cabin cabin = new Cabin(
            cabinId,
            request.name(),
            request.description(),
            categoryId,
            new Location(request.city(), request.state(), request.country()),
            new Capacity(request.maxGuests()),
            request.numberOfBedrooms(),
            request.numberOfBathrooms(),
            request.pricePerNight(),
            BigDecimal.ZERO,
            0,
            CabinStatus.AVAILABLE,
            true,
            List.of(new CabinImage("image-key", 1, true)),
            List.of(featureId),
            List.of(),
            LocalDateTime.now(),
            null
        );

        when(createCabinUseCase.execute(any(CreateCabinCommand.class))).thenReturn(cabin);
        when(generatePreSignedUrlUseCase.execute("image-key")).thenReturn("https://example.com/image-key");

        mockMvc.perform(post("/cabins")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(cabinId.toString()))
            .andExpect(jsonPath("$.name").value("Lake Cabin"))
            .andExpect(jsonPath("$.imageUrls[0]").value("https://example.com/image-key"));
    }

    @Test
    void shouldGetCabinDetails() throws Exception {
        UUID cabinId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        Cabin cabin = new Cabin(
            cabinId,
            "Forest Retreat",
            "Quiet cabin",
            categoryId,
            new Location("Forest", "State", "Country"),
            new Capacity(6),
            3,
            2,
            new BigDecimal("180"),
            BigDecimal.valueOf(4.8),
            5,
            CabinStatus.AVAILABLE,
            true,
            List.of(new CabinImage("image-key", 1, true)),
            List.of(UUID.randomUUID()),
            List.of(),
            LocalDateTime.now(),
            null
        );

        when(getCabinDetailsUseCase.execute(cabinId.toString())).thenReturn(cabin);
        when(generatePreSignedUrlUseCase.execute("image-key")).thenReturn("https://example.com/image-key");

        mockMvc.perform(get("/cabins/" + cabinId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Forest Retreat"))
            .andExpect(jsonPath("$.location.city").value("Forest"))
            .andExpect(jsonPath("$.imageUrls[0]").value("https://example.com/image-key"));
    }

    @Test
    void shouldSearchCabins() throws Exception {
        UUID cabinId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        Cabin cabin = new Cabin(
            cabinId,
            "Mountain Cabin",
            "High altitude stay",
            categoryId,
            new Location("Mountain", "State", "Country"),
            new Capacity(5),
            2,
            2,
            new BigDecimal("300"),
            BigDecimal.ZERO,
            0,
            CabinStatus.AVAILABLE,
            true,
            List.of(new CabinImage("image-key", 1, true)),
            List.of(UUID.randomUUID()),
            List.of(),
            LocalDateTime.now(),
            null
        );

        when(searchCabinsUseCase.execute(any(SearchCabinsCommand.class))).thenReturn(List.of(cabin));
        when(generatePreSignedUrlUseCase.execute("image-key")).thenReturn("https://example.com/image-key");

        mockMvc.perform(get("/cabins/search")
                .param("city", "Mountain")
                .param("maxGuests", "5")
                .param("maxPrice", "350"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Mountain Cabin"))
            .andExpect(jsonPath("$[0].location.city").value("Mountain"));
    }

    @Test
    void shouldUpdateCabinPricing() throws Exception {
        UUID cabinId = UUID.randomUUID();
        Cabin cabin = new Cabin(
            cabinId,
            "River Cabin",
            "Riverfront stay",
            UUID.randomUUID(),
            new Location("River", "State", "Country"),
            new Capacity(4),
            2,
            2,
            new BigDecimal("220"),
            BigDecimal.ZERO,
            0,
            CabinStatus.AVAILABLE,
            true,
            List.of(new CabinImage("image-key", 1, true)),
            List.of(UUID.randomUUID()),
            List.of(),
            LocalDateTime.now(),
            null
        );

        UpdatePricingRequest request = new UpdatePricingRequest(new BigDecimal("245"));
        when(updateCabinPricingUseCase.execute(anyString(), any(BigDecimal.class))).thenReturn(cabin);
        when(generatePreSignedUrlUseCase.execute("image-key")).thenReturn("https://example.com/image-key");

        mockMvc.perform(patch("/cabins/" + cabinId + "/pricing")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("River Cabin"));
    }
}
