package com.retreatreserve.infrastructure.adapter.in.rest.cabin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retreatreserve.application.command.feature.CreateFeatureCommand;
import com.retreatreserve.application.port.in.feature.CreateFeatureUseCase;
import com.retreatreserve.application.port.in.feature.GetAllFeaturesUseCase;
import com.retreatreserve.config.TestBaseConfig;
import com.retreatreserve.domain.model.cabin.Feature;
import com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.request.CreateFeatureRequest;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
    classes = FeatureControllerTest.TestConfig.class,
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc(addFilters = false)
class FeatureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GetAllFeaturesUseCase getAllFeaturesUseCase;

    @MockitoBean
    private CreateFeatureUseCase createFeatureUseCase;

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
        FeatureController.class,
        TestBaseConfig.class
    })
    static class TestConfig {
    }

    @Test
    void shouldReturnAllFeatures() throws Exception {
        Feature feature = new Feature(
            UUID.randomUUID(),
            "Fireplace",
            "fireplace.png",
            "Cozy indoor fireplace",
            true,
            LocalDateTime.now(),
            null
        );

        when(getAllFeaturesUseCase.execute()).thenReturn(List.of(feature));

        mockMvc.perform(get("/features"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Fireplace"))
            .andExpect(jsonPath("$[0].iconUrl").value("fireplace.png"));
    }

    @Test
    void shouldCreateFeature() throws Exception {
        CreateFeatureRequest request = new CreateFeatureRequest("Sauna", "sauna.png", "Relaxing sauna");
        Feature feature = new Feature(
            UUID.randomUUID(),
            request.name(),
            request.iconKey(),
            request.description(),
            true,
            LocalDateTime.now(),
            null
        );

        when(createFeatureUseCase.execute(any(CreateFeatureCommand.class))).thenReturn(feature);

        mockMvc.perform(post("/features")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Sauna"))
            .andExpect(jsonPath("$.description").value("Relaxing sauna"));
    }
}
