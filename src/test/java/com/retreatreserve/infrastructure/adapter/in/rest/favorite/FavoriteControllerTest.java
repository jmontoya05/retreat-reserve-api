package com.retreatreserve.infrastructure.adapter.in.rest.favorite;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retreatreserve.application.command.favorite.AddFavoriteCommand;
import com.retreatreserve.application.port.in.favorite.AddFavoriteUseCase;
import com.retreatreserve.application.port.in.favorite.GetUserFavoritesUseCase;
import com.retreatreserve.application.port.in.favorite.RemoveFavoriteUseCase;
import com.retreatreserve.config.TestBaseConfig;
import com.retreatreserve.domain.model.reservation.Favorite;
import com.retreatreserve.infrastructure.adapter.in.rest.favorite.dto.request.AddFavoriteRequest;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
    classes = FavoriteControllerTest.TestConfig.class,
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc(addFilters = false)
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AddFavoriteUseCase addFavoriteUseCase;

    @MockitoBean
    private RemoveFavoriteUseCase removeFavoriteUseCase;

    @MockitoBean
    private GetUserFavoritesUseCase getUserFavoritesUseCase;

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
        FavoriteController.class,
        TestBaseConfig.class
    })
    static class TestConfig {
    }

    @Test
    void shouldAddFavorite() throws Exception {
        UUID favoriteId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID cabinId = UUID.randomUUID();
        AddFavoriteRequest request = new AddFavoriteRequest(cabinId.toString());

        Favorite favorite = new Favorite(favoriteId, userId, cabinId, LocalDateTime.now());
        when(addFavoriteUseCase.execute(any(AddFavoriteCommand.class))).thenReturn(favorite);

        mockMvc.perform(post("/favorites")
                .header("X-User-Id", userId.toString())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").value(favoriteId.toString()));
    }

    @Test
    void shouldRemoveFavorite() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID cabinId = UUID.randomUUID();

        mockMvc.perform(delete("/favorites/cabin/" + cabinId)
                .header("X-User-Id", userId.toString()))
            .andExpect(status().isNoContent());

        verify(removeFavoriteUseCase).execute(userId.toString(), cabinId.toString());
    }

    @Test
    void shouldReturnUserFavorites() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID cabinId = UUID.randomUUID();
        Favorite favorite = new Favorite(UUID.randomUUID(), userId, cabinId, LocalDateTime.now());

        when(getUserFavoritesUseCase.execute(userId.toString())).thenReturn(List.of(favorite));

        mockMvc.perform(get("/favorites/user/" + userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").value(cabinId.toString()));
    }
}
