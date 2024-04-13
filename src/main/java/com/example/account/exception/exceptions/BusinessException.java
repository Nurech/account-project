package com.example.account.exception.exceptions;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final String code;
    private final String message;

    public BusinessException(ErrorCode errorCode, Object... args) {
        super(errorCode.formatMessage(args));
        this.code = errorCode.getCode();
        this.message = super.getMessage();
    }
}
