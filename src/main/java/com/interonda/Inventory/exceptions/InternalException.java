package com.interonda.Inventory.exceptions;

import java.time.LocalDateTime;

public class InternalException extends RuntimeException {
    private String userMessage;
    private LocalDateTime timestamp;

    public InternalException() {
        super("Ocurrió un error interno en el servidor.");
        this.timestamp = LocalDateTime.now();
    }

    public InternalException(String userMessage) {
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
