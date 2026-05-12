package com.retreatreserve.infrastructure.exception;

import com.retreatreserve.domain.exception.cabin.*;
import com.retreatreserve.domain.exception.cabin.CabinNotAvailableException;
import com.retreatreserve.domain.exception.reservation.*;
import com.retreatreserve.domain.exception.user.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String NOT_FOUND = "Not Found";
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiError(404, NOT_FOUND, ex.getMessage()));
    }
    
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleEmailExists(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ApiError(409, "Conflict", ex.getMessage()));
    }
    
    @ExceptionHandler(CabinNotFoundException.class)
    public ResponseEntity<ApiError> handleCabinNotFound(CabinNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiError(404, NOT_FOUND, ex.getMessage()));
    }
    
    @ExceptionHandler(CabinNotAvailableException.class)
    public ResponseEntity<ApiError> handleCabinNotAvailable(CabinNotAvailableException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ApiError(409, "Conflict", ex.getMessage()));
    }
    
    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ApiError> handleReservationNotFound(ReservationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiError(404, NOT_FOUND, ex.getMessage()));
    }
    
    @ExceptionHandler(InvalidRatingException.class)
    public ResponseEntity<ApiError> handleInvalidRating(InvalidRatingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiError(400, "Bad Request", ex.getMessage()));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .findFirst()
            .orElse("Validation failed");
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiError(400, "Validation Error", message));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiError(500, "Internal Server Error", "An unexpected error occurred"));
    }
}
