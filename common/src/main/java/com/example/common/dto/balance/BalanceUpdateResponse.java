package com.example.common.dto.balance;

import com.example.common.model.Balance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceUpdateResponse {
    private Balance balance;
}