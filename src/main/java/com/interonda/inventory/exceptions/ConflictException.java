package com.interonda.inventory.exceptions;

import java.time.LocalDateTime;

public class ConflictException extends RuntimeException {
    private final String userMessage;
    private final LocalDateTime timestamp;

    public ConflictException() {
        this("Conflicto al procesar la solicitud.");
    }

    public ConflictException(String userMessage) {
        super(userMessage);
        this.userMessage = userMessage;
        this.timestamp = LocalDateTime.now();
    }

    public ConflictException(String userMessage, Throwable cause) {
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


