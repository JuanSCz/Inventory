package com.interonda.Inventory.exceptions;

import java.time.LocalDateTime;

public class DataAccessException extends RuntimeException {
    private final String userMessage;
    private final LocalDateTime timestamp;

    public DataAccessException() {
        this("Error al acceder a los datos.");
    }

    public DataAccessException(String userMessage) {
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