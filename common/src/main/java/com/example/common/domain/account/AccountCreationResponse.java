package com.example.common.domain.account;

import com.example.common.dto.ErrorDTO;
import com.example.common.model.Balance;
import lombok.*;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreationResponse implements Serializable {
    private Long accountId;
    private Long customerId;
    private List<Balance> balances;
}
