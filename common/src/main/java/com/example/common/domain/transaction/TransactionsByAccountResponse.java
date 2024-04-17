package com.example.common.domain.transaction;

import com.example.common.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionsByAccountResponse implements Serializable {
    private Long accountId;
    private List<Transaction> transactions;
}
