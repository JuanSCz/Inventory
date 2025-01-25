package com.interonda.inventory.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private ResponseEntity<Map<String, Object>> buildResponse(String title, String message, HttpStatus status) {
        Map<String, Object> response = Map.of("title", title, "message", message, "timestamp", LocalDateTime.now());
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

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(UnauthorizedException ex) {
        logger.warn("Unauthorized Error: {} | Timestamp: {}", ex.getUserMessage(), ex.getTimestamp());
        return buildResponse("Error: Unauthorized", ex.getUserMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<Map<String, Object>> handleInternalException(InternalException ex) {
        logger.error("Internal Server Error: {} | Timestamp: {}", ex.getUserMessage(), ex.getTimestamp());
        return buildResponse("Error: Problema interno del sistema", ex.getUserMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ModelAndView handleAuthenticationException(AuthenticationException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return new ModelAndView("index", model.asMap());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(ConstraintViolationException ex, Model model, HttpServletRequest request) {
        logger.warn("Constraint Violation: {}", ex.getMessage());
        Locale locale = LocaleContextHolder.getLocale();
        String errorMessage = ex.getConstraintViolations().stream().map(violation -> messageSource.getMessage(violation.getMessage(), null, locale)).collect(Collectors.joining("<br>"));
        model.addAttribute("errorMessage", errorMessage);
        return "redirect:" + request.getHeader("Referer");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException ex, Model model, HttpServletRequest request) {
        Locale locale = LocaleContextHolder.getLocale();
        String errorMessage = ex.getBindingResult().getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, locale)).collect(Collectors.joining("<br>"));
        model.addAttribute("errorMessage", errorMessage);
        return "redirect:" + request.getHeader("Referer");
    }
}