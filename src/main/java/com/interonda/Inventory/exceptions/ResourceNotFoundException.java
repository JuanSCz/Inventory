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

    public String getUserMessage() {
        return userMessage;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

