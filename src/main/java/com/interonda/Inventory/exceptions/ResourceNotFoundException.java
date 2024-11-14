package com.interonda.Inventory.exceptions;

import java.time.LocalDateTime;

public class ResourceNotFoundException extends RuntimeException {
    private final String userMessage;
    private final LocalDateTime timestamp;

    public ResourceNotFoundException() {
        this("Recurso no encontrado.");
    }

    public ResourceNotFoundException(String userMessage) {
        super(userMessage);
        this.userMessage = userMessage;
        this.timestamp = LocalDateTime.now();
    }

    public ResourceNotFoundException(String userMessage, Throwable cause) {
        super(userMessage, cause);
        this.userMessage = userMessage;
        this.timestamp = LocalDateTime.now();
    }

    public String getUserMessage() {
        return userMessage;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

