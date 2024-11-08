package com.interonda.Inventory.exceptions;

import java.time.LocalDateTime;

public class ConflictException extends RuntimeException {
    private String userMessage;
    private LocalDateTime timestamp;

    public ConflictException() {
        super("Conflicto al procesar la solicitud.");
        this.timestamp = LocalDateTime.now();
    }

    public ConflictException(String userMessage) {
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


