package com.example.common.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class Transaction {
    private Long id;
    private Long accountId;
    private BigDecimal amount;
    private String currency;
    private String transactionDirection;
    private String description;
    private ZonedDateTime transactionDate;
}
