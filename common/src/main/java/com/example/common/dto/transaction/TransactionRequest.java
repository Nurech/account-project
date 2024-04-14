package com.example.common.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest implements Serializable {
    private Long accountId;
    private BigDecimal amount;
    private String currency;
    private String transactionDirection; // "IN" or "OUT"
    private String description;
}
