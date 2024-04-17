package com.example.common.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class Transaction implements Serializable {
    private Long id;
    private Long accountId;
    private BigDecimal amount;
    private String currency;
    private String transactionDirection;
    private String description;
    private OffsetDateTime transactionDate;
}
