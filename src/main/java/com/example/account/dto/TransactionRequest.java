package com.example.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private Long accountId;
    private BigDecimal amount;
    private String currency;
    private String transactionDirection; // "IN" or "OUT"
    private String description;
}
