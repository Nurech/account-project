package com.example.common.exception.exceptions;

import lombok.Getter;

@Getter
public class NoResponseFromRabbitException extends RuntimeException {

    private final Object request;

    public NoResponseFromRabbitException(Object request) {
        super("No response received from the message broker for the request: " + request);
        this.request = request;
    }

}
