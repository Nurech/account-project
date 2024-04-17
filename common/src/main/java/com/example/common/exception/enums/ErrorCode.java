package com.example.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_CURRENCY("1001", "Invalid currency."),
    INSUFFICIENT_FUNDS("1002", "Insufficient funds for this transaction."),
    ACCOUNT_NOT_FOUND("1003", "Invalid account. The specified account could not be found."),
    INVALID_REQUEST("1004", "The request is invalid."),
    TRANSACTION_ERROR("1005", "Error processing transaction."),
    NEGATIVE_AMOUNT("1006", "The amount must be positive."),
    INVALID_DIRECTION("1007", "Transaction direction is invalid and must be 'IN' or 'OUT'."),
    RABBIT_NO_ANSWER("1008", "No response from Rabbit."),
    TRANSACTION_PROCESSING_ERROR("1008", "Unknown error occurred while processing transaction."),
    UNKNOWN_ERROR("1009", "Unknown error occurred."),
    INVALID_ACCOUNT("1010", "The specified account is does not exist."),
    NO_TRANSACTIONS("1011", "No transactions found for the specified account."),
    BALANCE_MISSING("1012", "Balance not found for the specified account and currency."),
    DESCRIPTION_MISSING("1013", "Description is required.");

    private final String code;
    private final String message;

    public static ErrorCode fromCode(String code) {
        for (ErrorCode e : ErrorCode.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        throw new IllegalArgumentException("No enum constant for code: " + code);
    }

    public String formatMessage(Object... args) {
        return String.format(message, args);
    }
}
