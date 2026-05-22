package com.retreatreserve.infrastructure.adapter.in.rest.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retreatreserve.application.dto.iam.LoginResult;
import com.retreatreserve.application.port.in.iam.LoginUseCase;
import com.retreatreserve.application.port.in.iam.RegisterUserUseCase;
import com.retreatreserve.application.port.in.iam.VerifyEmailUseCase;
import com.retreatreserve.config.TestBaseConfig;
import com.retreatreserve.domain.model.iam.*;
import com.retreatreserve.infrastructure.adapter.in.rest.iam.AuthController;
import com.retreatreserve.infrastructure.adapter.in.rest.iam.dto.request.LoginRequest;
import com.retreatreserve.infrastructure.adapter.in.rest.iam.dto.request.RegisterRequest;
import com.retreatreserve.infrastructure.adapter.in.rest.mapper.UserDtoMapper;
import org.springframework.http.MediaType;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest(
    classes = AuthControllerTest.TestConfig.class,
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockitoBean
    private RegisterUserUseCase registerUserUseCase;
    
    @MockitoBean
    private LoginUseCase loginUseCase;

    @MockitoBean
    private VerifyEmailUseCase verifyEmailUseCase;

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
        AuthController.class,
        UserDtoMapper.class,
        TestBaseConfig.class
    })
    static class TestConfig {
    }
    
    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        RegisterRequest request = new RegisterRequest(
            "John", "Doe", "john@example.com", "password123", "+1234567890"
        );
        
        User user = createTestUser();
        when(registerUserUseCase.execute(any())).thenReturn(user);
        
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value("john@example.com"));
    }
    
    @Test
    void shouldLoginSuccessfully() throws Exception {
        LoginRequest request = new LoginRequest("john@example.com", "password123");
        
        User user = createTestUser();
        LoginResult result = new LoginResult("jwt-token", user);
        
        when(loginUseCase.execute(any(), any())).thenReturn(result);
        
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("jwt-token"))
            .andExpect(jsonPath("$.email").value("john@example.com"));
    }
    
    @Test
    void shouldValidateRegisterRequest() throws Exception {
        RegisterRequest invalidRequest = new RegisterRequest(
            "", "", "invalid-email", "123", ""
        );
        
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());
    }
    
    private User createTestUser() {
        return new User(
            UUID.randomUUID(),
            new FullName("John", "Doe"),
            new Email("john@example.com"),
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
    }
}
