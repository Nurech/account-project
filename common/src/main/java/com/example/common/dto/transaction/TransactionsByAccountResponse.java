package com.example.common.dto.transaction;

import com.example.common.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionsByAccountResponse {
    private Long accountId;
    private List<Transaction> transactions;
}
