package com.example.common.model;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;

@Data
public class Balance {
    private Long id;
    private Long accountId;
    private String currency;
    private BigDecimal availableAmount;
}
