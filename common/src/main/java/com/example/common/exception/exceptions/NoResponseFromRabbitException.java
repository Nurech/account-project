package com.example.common.exception.exceptions;

public class NoResponseFromRabbitException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final Object request;

    public NoResponseFromRabbitException(Object request) {
        super("No response received from the message broker for the request: " + request);
        this.request = request;
    }

    public Object getRequest() {
        return request;
    }
}
