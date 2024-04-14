package com.example.common.exception;

import com.example.common.exception.exceptions.BusinessException;
import com.example.common.exception.exceptions.NoResponseFromRabbitException;
import com.example.common.exception.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

import static com.example.common.exception.enums.ErrorCode.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (Objects.equals(e.getCode(), ACCOUNT_NOT_FOUND.getCode())) {
            status = HttpStatus.NOT_FOUND; // 404
        }

        return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage()), status);
    }

    @ExceptionHandler(NoResponseFromRabbitException.class)
    public ResponseEntity<Object> handleNoResponseFromRabbitException(NoResponseFromRabbitException ignored) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorResponse(RABBIT_NO_ANSWER.getCode(), RABBIT_NO_ANSWER.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ignored) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(UNKNOWN_ERROR.getCode(), UNKNOWN_ERROR.getMessage()));
    }
}

