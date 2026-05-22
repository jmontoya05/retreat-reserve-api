package com.retreatreserve.infrastructure.adapter.in.rest.iam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retreatreserve.application.command.iam.ChangePasswordCommand;
import com.retreatreserve.application.command.iam.UpdateUserProfileCommand;
import com.retreatreserve.application.port.in.iam.ChangePasswordUseCase;
import com.retreatreserve.application.port.in.iam.GetUserByIdUseCase;
import com.retreatreserve.application.port.in.iam.UpdateUserProfileUseCase;
import com.retreatreserve.config.TestBaseConfig;
import com.retreatreserve.domain.model.iam.Email;
import com.retreatreserve.domain.model.iam.FullName;
import com.retreatreserve.domain.model.iam.Role;
import com.retreatreserve.domain.model.iam.User;
import com.retreatreserve.infrastructure.adapter.in.rest.iam.dto.request.ChangePasswordRequest;
import com.retreatreserve.infrastructure.adapter.in.rest.iam.dto.request.UpdateProfileRequest;
import com.retreatreserve.infrastructure.adapter.in.rest.mapper.UserDtoMapper;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
    classes = UserControllerTest.TestConfig.class,
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GetUserByIdUseCase getUserByIdUseCase;

    @MockitoBean
    private UpdateUserProfileUseCase updateUserProfileUseCase;

    @MockitoBean
    private ChangePasswordUseCase changePasswordUseCase;

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
        UserController.class,
        UserDtoMapper.class,
        TestBaseConfig.class
    })
    static class TestConfig {
    }

    @Test
    void shouldReturnUserProfile() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User(
            userId,
            new FullName("Jane", "Doe"),
            new Email("jane@example.com"),
            "hashed-password",
            "+1234567890",
            Role.USER,
            true,
            null,
            null,
            true,
            LocalDateTime.now(),
            null
        );

        when(getUserByIdUseCase.execute(userId.toString())).thenReturn(user);

        mockMvc.perform(get("/users/" + userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("jane@example.com"))
            .andExpect(jsonPath("$.firstName").value("Jane"))
            .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void shouldUpdateUserProfile() throws Exception {
        UUID userId = UUID.randomUUID();
        UpdateProfileRequest request = new UpdateProfileRequest("Janet", "Smith", "+1987654321");

        User updatedUser = new User(
            userId,
            new FullName("Janet", "Smith"),
            new Email("jane@example.com"),
            "hashed-password",
            request.phoneNumber(),
            Role.USER,
            true,
            null,
            null,
            true,
            LocalDateTime.now(),
            null
        );

        when(updateUserProfileUseCase.execute(any(UpdateUserProfileCommand.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/" + userId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Janet"))
            .andExpect(jsonPath("$.phoneNumber").value("+1987654321"));
    }

    @Test
    void shouldChangePassword() throws Exception {
        UUID userId = UUID.randomUUID();
        ChangePasswordRequest request = new ChangePasswordRequest("oldpass", "newpass");

        doNothing().when(changePasswordUseCase).execute(any(ChangePasswordCommand.class));

        mockMvc.perform(post("/users/" + userId + "/change-password")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNoContent());
    }
}
