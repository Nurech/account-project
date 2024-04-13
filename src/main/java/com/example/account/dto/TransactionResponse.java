package com.example.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long accountId;
    private Long transactionId;
    private BigDecimal amount;
    private String currency;
    private String transactionDirection;
    private String description;
    private BigDecimal balanceAfterTransaction;
}