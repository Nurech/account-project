package com.example.common.dto.balance;

import com.example.common.model.Balance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetBalanceByCurrencyResponse implements Serializable {
    private Long accountId;
    private Balance balance;
}