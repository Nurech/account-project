package com.example.common.exception.exceptions;

import com.example.common.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    public final String code;
    public final String message;

    public BusinessException(ErrorCode errorCode, Object... args) {
        super(errorCode.formatMessage(args));
        this.code = errorCode.getCode();
        this.message = super.getMessage();
    }
}
