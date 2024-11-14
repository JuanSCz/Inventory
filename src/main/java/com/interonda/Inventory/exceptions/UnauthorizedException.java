package com.interonda.Inventory.exceptions;

import java.time.LocalDateTime;

public class UnauthorizedException extends RuntimeException {
    private final String userMessage;
    private final LocalDateTime timestamp;

    public UnauthorizedException() {
        this("Acceso no autorizado.");
    }

    public UnauthorizedException(String userMessage) {
        super(userMessage);
        this.userMessage = userMessage;
        this.timestamp = LocalDateTime.now();
    }

    public UnauthorizedException(String userMessage, Throwable cause) {
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