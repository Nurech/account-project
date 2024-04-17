package com.example.common.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Balance implements Serializable {
    private Long id;
    private Long accountId;
    private String currency;
    private BigDecimal availableAmount;
}
