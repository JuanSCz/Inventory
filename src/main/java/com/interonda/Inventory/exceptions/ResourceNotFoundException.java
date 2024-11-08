package com.interonda.Inventory.exceptions;

import java.time.LocalDateTime;

public class ResourceNotFoundException extends RuntimeException {
    private String userMessage;
    private LocalDateTime timestamp;

    public ResourceNotFoundException() {
        super("Recurso no encontrado.");
        this.timestamp = LocalDateTime.now();
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

