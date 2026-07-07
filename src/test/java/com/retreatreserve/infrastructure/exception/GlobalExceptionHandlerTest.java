package com.retreatreserve.infrastructure.exception;

import com.retreatreserve.application.exception.storage.InvalidImageException;
import com.retreatreserve.domain.exception.user.InvalidCredentialsException;
import com.retreatreserve.domain.exception.user.UserNotFoundException;
import com.retreatreserve.infrastructure.exception.notification.EmailSendingException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldMapUserNotFoundExceptionToNotFound() {
        ResponseEntity<ApiError> response = handler.handleDomainException(new UserNotFoundException("User not found"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().status());
        assertEquals("Not Found", response.getBody().error());
        assertEquals("User not found", response.getBody().message());
    }

    @Test
    void shouldMapInvalidCredentialsExceptionToUnauthorized() {
        ResponseEntity<ApiError> response = handler
                .handleInvalidCredentials(new InvalidCredentialsException("Invalid credentials"));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(401, response.getBody().status());
        assertEquals("Unauthorized", response.getBody().error());
        assertEquals("Invalid credentials", response.getBody().message());
    }

    @Test
    void shouldMapIllegalArgumentExceptionToBadRequest() {
        ResponseEntity<ApiError> response = handler
                .handleIllegalArgument(new IllegalArgumentException("Invalid input"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().status());
        assertEquals("Bad Request", response.getBody().error());
        assertEquals("Invalid input", response.getBody().message());
    }

    @Test
    void shouldMapMethodArgumentNotValidToBadRequest() {
        BindingResult bindingResult = new BeanPropertyBindingResult(new SamplePayload("", ""), "samplePayload");
        bindingResult.rejectValue("name", "NotBlank", "must not be blank");
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ApiError> response = handler.handleValidation(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().status());
        assertEquals("Validation Error", response.getBody().error());
        assertNotNull(response.getBody().message());
    }

    @Test
    void shouldIncludeAllValidationErrorsInMessage() {
        BindingResult bindingResult = new BeanPropertyBindingResult(new SamplePayload("", ""), "samplePayload");
        bindingResult.rejectValue("name", "NotBlank", "must not be blank");
        bindingResult.rejectValue("email", "Email", "must be a valid email");
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ApiError> response = handler.handleValidation(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("must not be blank, must be a valid email", response.getBody().message());
    }

    @Test
    void shouldMapInvalidImageExceptionToBadRequest() {
        ResponseEntity<ApiError> response = handler
                .handleInvalidImage(new InvalidImageException("Invalid image payload", new RuntimeException("boom")));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().status());
        assertEquals("Bad Request", response.getBody().error());
        assertEquals("Invalid image payload", response.getBody().message());
    }

    @Test
    void shouldMapEmailSendingExceptionToBadGateway() {
        ResponseEntity<ApiError> response = handler
                .handleEmailSending(new EmailSendingException("Failed to send email", new RuntimeException("smtp")));

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        assertEquals(502, response.getBody().status());
        assertEquals("Bad Gateway", response.getBody().error());
        assertEquals("Failed to send email", response.getBody().message());
    }

    private static class SamplePayload {
        private String name;
        private String email;

        private SamplePayload(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
}
