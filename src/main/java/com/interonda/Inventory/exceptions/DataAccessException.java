package com.interonda.Inventory.exceptions;

import java.time.LocalDateTime;

public class DataAccessException extends RuntimeException {
    private String userMessage;
    private LocalDateTime timestamp;

    public DataAccessException() {
        super("Error al acceder a los datos.");
        this.timestamp = LocalDateTime.now();
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

