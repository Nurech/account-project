package com.example.common.domain.balance;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetAccountBalancesRequest implements Serializable {
    private Long accountId;
}