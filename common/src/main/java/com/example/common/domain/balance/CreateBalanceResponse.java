package com.example.common.domain.balance;

import com.example.common.dto.ErrorDTO;
import com.example.common.model.Balance;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBalanceResponse implements Serializable {
    private Long accountId;
    private List<Balance> balances;
}