package com.example.common.domain.balance;

import com.example.common.dto.ErrorDTO;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetBalanceByCurrencyRequest implements Serializable {
    private Long accountId;
    private String currency;
}