package com.retreatreserve.infrastructure.adapter.in.rest.cabin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retreatreserve.application.command.category.CreateCategoryCommand;
import com.retreatreserve.application.port.in.category.CreateCategoryUseCase;
import com.retreatreserve.application.port.in.category.GetAllCategoriesUseCase;
import com.retreatreserve.application.port.in.storage.GeneratePreSignedUrlUseCase;
import com.retreatreserve.config.TestBaseConfig;
import com.retreatreserve.domain.model.cabin.Category;
import com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.request.CreateCategoryRequest;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
    classes = CategoryControllerTest.TestConfig.class,
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GetAllCategoriesUseCase getAllCategoriesUseCase;

    @MockitoBean
    private CreateCategoryUseCase createCategoryUseCase;

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
        CategoryController.class,
        TestBaseConfig.class
    })
    static class TestConfig {
    }

    @Test
    void shouldReturnAllCategories() throws Exception {
        Category category = new Category(
            UUID.randomUUID(),
            "Beach",
            "Beach cabins",
            "beach.jpg",
            true,
            LocalDateTime.now(),
            null
        );

        when(getAllCategoriesUseCase.execute()).thenReturn(List.of(category));

        mockMvc.perform(get("/categories"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Beach"))
            .andExpect(jsonPath("$[0].description").value("Beach cabins"));
    }

    @Test
    void shouldCreateCategory() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest("Forest", "Forest cabins", "forest.jpg");
        Category category = new Category(
            UUID.randomUUID(),
            request.name(),
            request.description(),
            request.imageKey(),
            true,
            LocalDateTime.now(),
            null
        );

        when(createCategoryUseCase.execute(any(CreateCategoryCommand.class))).thenReturn(category);
        when(generatePreSignedUrlUseCase.execute(anyString())).thenReturn("https://example.com/forest.jpg");

        mockMvc.perform(post("/categories")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Forest"))
            .andExpect(jsonPath("$.imageUrl").value("https://example.com/forest.jpg"));
    }
}
