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
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Cuando los datos proporcionados por el cliente son incorrectos, incompletos o no cumplen con las validaciones requeridas.
    // Ejemplo: Si un campo obligatorio, como nombre, está vacío y no cumple con una validación específica.
    @ExceptionHandler(BadRequestException.class) // (400)
    public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException ex) {

        logger.warn("Bad Request: {} | Timestamp: {}", ex.getUserMessage(), ex.getTimestamp());

        Map<String, Object> response = new HashMap<>();
        response.put("title", "Error: Solicitud incorrecta"); // Título de advertencia
        response.put("message", ex.getUserMessage()); // Mensaje claro para el usuario
        response.put("timestamp", ex.getTimestamp()); // Fecha y hora del error

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Cuando no se encuentra un recurso específico en la base de datos.
    // Ejemplo: por ejemplo, al buscar un cliente por ID que no existe.
    @ExceptionHandler(ResourceNotFoundException.class) // (404)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {

        logger.warn("Resource Not Found: {} | Timestamp: {}", ex.getUserMessage(), ex.getTimestamp());

        Map<String, Object> response = new HashMap<>();
        response.put("title", "Error: Recurso no encontrado");
        response.put("message", ex.getUserMessage());
        response.put("timestamp", ex.getTimestamp());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Cuando hay un conflicto al intentar realizar una operación que ya está en conflicto con un recurso existente.
    // Ejemplo: por ejemplo, un cliente con el mismo contacto ya registrado
    @ExceptionHandler(ConflictException.class) // (409)
    public ResponseEntity<Map<String, Object>> handleConflictException(ConflictException ex) {

        logger.warn("Conflict Error: {} | Timestamp: {}", ex.getUserMessage(), ex.getTimestamp());

        Map<String, Object> response = new HashMap<>();
        response.put("title", "Error: Conflicto en la solicitud");
        response.put("message", ex.getUserMessage());
        response.put("timestamp", ex.getTimestamp());

        // Devolver la respuesta al cliente
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Cuando ocurre un error relacionado con la base de datos, como problemas de acceso, fallos en consultas o problemas con transacciones.
    // Ejemplo: Cuando una operación de guardado o actualización falla debido a una restricción de integridad de la base de datos.
    @ExceptionHandler(DataAccessException.class) // (500)
    public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException ex) {

        logger.warn("Data Access Error: {} | Timestamp: {}", ex.getUserMessage(), ex.getTimestamp());

        Map<String, Object> response = new HashMap<>();
        response.put("title", "Error: Acceso a datos fallido");
        response.put("message", ex.getUserMessage());
        response.put("timestamp", ex.getTimestamp());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Para errores internos no anticipados que no encajan en otras categorías, como fallos en la lógica de negocio o situaciones no manejadas.
    @ExceptionHandler(InternalException.class) // (500)
    public ResponseEntity<Map<String, Object>> handleInternalException(InternalException ex) {

        logger.error("Internal Server Error: {} | Timestamp: {}", ex.getUserMessage(), ex.getTimestamp());

        Map<String, Object> response = new HashMap<>();
        response.put("title", "Error: Problema interno del sistema");
        response.put("message", ex.getUserMessage());
        response.put("timestamp", ex.getTimestamp());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // Manejo de excepciones generales (Exception.class) para capturar cualquier otra excepción no específica
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        logger.error("Unexpected Error: {} | Message: {}", ex.getMessage(), ex.getClass().getName());

        Map<String, Object> response = new HashMap<>();
        response.put("title", "Error: Ocurrió un problema inesperado");
        response.put("message", "Ocurrió un error inesperado. Por favor, intente de nuevo más tarde.");
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // Cuando ocurren errores de validación de campos con anotaciones (por ejemplo, @NotBlank)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        logger.warn("Validation Error: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // Cuando se violan restricciones de validación a nivel de entidad. Esta excepción es lanzada automáticamente por el EntityManager de JPA.
    // Ejemplo: Si tienes una entidad con @Size(min = 3, max = 50), Spring lanzará esta excepción si el tamaño no es el esperado.
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {

        logger.warn("Constraint Violation: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> errors.put(violation.getPropertyPath().toString(), violation.getMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}