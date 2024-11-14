package com.interonda.Inventory.exceptions;

import java.time.LocalDateTime;

public class BadRequestException extends RuntimeException {
    private final String userMessage;
    private final LocalDateTime timestamp;

    public BadRequestException() {
        this("Solicitud incorrecta.");
    }

    public BadRequestException(String userMessage) {
        super(userMessage);
        this.userMessage = userMessage;
        this.timestamp = LocalDateTime.now();
    }

    public BadRequestException(String userMessage, Throwable cause) {
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

