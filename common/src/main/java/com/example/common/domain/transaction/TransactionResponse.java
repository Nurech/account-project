package com.example.common.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse implements Serializable {
    private Long accountId;
    private Long transactionId;
    private BigDecimal amount;
    private String currency;
    private String transactionDirection;
    private String description;
    private BigDecimal balanceAfterTransaction;
}