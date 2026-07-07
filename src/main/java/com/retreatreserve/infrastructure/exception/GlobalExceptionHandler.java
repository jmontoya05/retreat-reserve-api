package com.retreatreserve.infrastructure.exception;

import com.retreatreserve.application.exception.storage.InvalidImageException;
import com.retreatreserve.domain.exception.DomainException;
import com.retreatreserve.domain.exception.cabin.*;
import com.retreatreserve.domain.exception.cabin.CabinNotAvailableException;
import com.retreatreserve.domain.exception.reservation.*;
import com.retreatreserve.domain.exception.user.*;
import com.retreatreserve.infrastructure.exception.notification.EmailSendingException;
import com.retreatreserve.infrastructure.exception.storage.ImageUploadingException;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String NOT_FOUND = "Not Found";
    private static final String CONFLICT = "Conflict";
    private static final String BAD_REQUEST = "Bad Request";
    private static final String UNAUTHORIZED = "Unauthorized";
    private static final String FORBIDDEN = "Forbidden";
    private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleInvalidCredentials(InvalidCredentialsException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handleDomainException(DomainException ex) {
        HttpStatus status = resolveDomainStatus(ex);
        return buildResponse(status, resolveErrorName(status), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation Error",
                buildValidationMessage(ex.getBindingResult()));
    }

    @ExceptionHandler({ BindException.class, MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class, HttpMessageNotReadableException.class,
            ConstraintViolationException.class })
    public ResponseEntity<ApiError> handleBadRequest(Exception ex) {
        String message = ex.getMessage();
        if (ex instanceof MethodArgumentTypeMismatchException mismatch) {
            message = "Invalid value for parameter '" + mismatch.getName() + "'";
        } else if (ex instanceof HttpMessageNotReadableException) {
            message = "Malformed request body";
        } else if (ex instanceof MissingServletRequestParameterException missing) {
            message = "Missing required parameter: " + missing.getParameterName();
        } else if (ex instanceof ConstraintViolationException) {
            message = "Validation failed";
        } else if (ex instanceof BindException bindException) {
            message = buildValidationMessage(bindException.getBindingResult());
        }

        return buildResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, message);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthentication(AuthenticationException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InvalidImageException.class)
    public ResponseEntity<ApiError> handleInvalidImage(InvalidImageException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ImageUploadingException.class)
    public ResponseEntity<ApiError> handleImageUploading(ImageUploadingException ex) {
        return buildResponse(HttpStatus.BAD_GATEWAY, "Bad Gateway", ex.getMessage());
    }

    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<ApiError> handleEmailSending(EmailSendingException ex) {
        return buildResponse(HttpStatus.BAD_GATEWAY, "Bad Gateway", ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return buildResponse(HttpStatus.CONFLICT, CONFLICT, "A data integrity violation occurred");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneral(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String error, String message) {
        return ResponseEntity.status(status).body(new ApiError(status.value(), error, message));
    }

    private String buildValidationMessage(BindingResult bindingResult) {
        if (bindingResult == null || bindingResult.getAllErrors().isEmpty()) {
            return "Validation failed";
        }

        return bindingResult.getAllErrors().stream()
                .map(error -> error.getDefaultMessage() == null ? error.getCode() : error.getDefaultMessage())
                .distinct()
                .collect(java.util.stream.Collectors.joining(", "));
    }

    private HttpStatus resolveDomainStatus(DomainException ex) {
        if (ex instanceof UserNotFoundException
                || ex instanceof CabinNotFoundException
                || ex instanceof ReservationNotFoundException
                || ex instanceof CategoryNotFoundException
                || ex instanceof FeatureNotFoundException
                || ex instanceof ReviewNotFoundException) {
            return HttpStatus.NOT_FOUND;
        }

        if (ex instanceof EmailAlreadyExistsException
                || ex instanceof CategoryAlreadyExistsException
                || ex instanceof FeatureAlreadyExistsException
                || ex instanceof DuplicateFeatureException
                || ex instanceof FavoriteAlreadyExistsException
                || ex instanceof ReviewAlreadyExistsException
                || ex instanceof CabinNotAvailableException) {
            return HttpStatus.CONFLICT;
        }

        return HttpStatus.BAD_REQUEST;
    }

    private String resolveErrorName(HttpStatus status) {
        return switch (status) {
            case NOT_FOUND -> NOT_FOUND;
            case CONFLICT -> CONFLICT;
            case UNAUTHORIZED -> UNAUTHORIZED;
            case FORBIDDEN -> FORBIDDEN;
            case BAD_REQUEST -> BAD_REQUEST;
            default -> INTERNAL_SERVER_ERROR;
        };
    }
}
