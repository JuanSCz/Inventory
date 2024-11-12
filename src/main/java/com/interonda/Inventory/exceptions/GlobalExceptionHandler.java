package com.interonda.Inventory.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<Map<String, Object>> buildResponse(String title, String message, HttpStatus status) {
        Map<String, Object> response = Map.of(
                "title", title,
                "message", message,
                "timestamp", LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException ex) {
        logger.warn("Bad Request: {} | Timestamp: {}", ex.getUserMessage(), ex.getTimestamp());
        return buildResponse("Error: Solicitud incorrecta", ex.getUserMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.warn("Resource Not Found: {} | Timestamp: {}", ex.getUserMessage(), ex.getTimestamp());
        return buildResponse("Error: Recurso no encontrado", ex.getUserMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflictException(ConflictException ex) {
        logger.warn("Conflict Error: {} | Timestamp: {}", ex.getUserMessage(), ex.getTimestamp());
        return buildResponse("Error: Conflicto en la solicitud", ex.getUserMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException ex) {
        logger.warn("Data Access Error: {} | Timestamp: {}", ex.getUserMessage(), ex.getTimestamp());
        return buildResponse("Error: Acceso a datos fallido", ex.getUserMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<Map<String, Object>> handleInternalException(InternalException ex) {
        logger.error("Internal Server Error: {} | Timestamp: {}", ex.getUserMessage(), ex.getTimestamp());
        return buildResponse("Error: Problema interno del sistema", ex.getUserMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        logger.error("Unexpected Error: {} | Message: {}", ex.getMessage(), ex.getClass().getName());
        return buildResponse("Error: Ocurrió un problema inesperado", "Ocurrió un error inesperado. Por favor, intente de nuevo más tarde.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.warn("Validation Error: {}", ex.getMessage());
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        logger.warn("Constraint Violation: {}", ex.getMessage());
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage()
                ));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}