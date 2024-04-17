package com.example.common.domain.balance;

import com.example.common.model.Balance;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBalanceRequest implements Serializable {
    private Balance balance;
}