package com.example.account.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Balance {
    private Long id;
    private Long accountId;
    private String currency;
    private BigDecimal availableAmount;
}
