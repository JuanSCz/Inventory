package com.interonda.Inventory.exceptions;

import java.time.LocalDateTime;

public class BadRequestException extends RuntimeException {
    private String userMessage;
    private LocalDateTime timestamp;

    public BadRequestException() {
        super("Solicitud incorrecta.");
        this.timestamp = LocalDateTime.now();
    }

    public BadRequestException(String userMessage) {
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

