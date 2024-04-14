package com.example.common.dto.balance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetBalanceByCurrencyRequest implements Serializable {
    private Long accountId;
    private String currency;
}