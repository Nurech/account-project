package com.example.account.dto;

import com.example.account.model.Balance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreationResponse {
    private Long accountId;
    private String customerId;
    private List<Balance> balances;
}
