package com.example.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_CURRENCY("1001", "Invalid currency: %s."),
    INSUFFICIENT_FUNDS("1002", "Insufficient funds for this transaction."),
    ACCOUNT_NOT_FOUND("1003", "The specified account could not be found."),
    INVALID_REQUEST("1004", "The request is invalid."),
    TRANSACTION_ERROR("1005", "Error processing transaction."),
    NEGATIVE_AMOUNT("1006", "The amount must be positive."),
    INVALID_DIRECTION("1007", "Transaction direction is invalid and must be 'IN' or 'OUT'."),
    TRANSACTION_PROCESSING_ERROR("1008", "Unknown error occurred while processing transaction.");

    private final String code;
    private final String message;

    public String formatMessage(Object... args) {
        return String.format(message, args);
    }
}
