package com.example.common.dto.account;

import com.example.common.model.Balance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreationResponse implements Serializable {
    private Long accountId;
    private Long customerId;
    private List<Balance> balances;
}
