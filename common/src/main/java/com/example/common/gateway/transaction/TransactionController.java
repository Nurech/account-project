package com.example.common.gateway.transaction;

import com.example.common.domain.transaction.TransactionRequest;
import com.example.common.domain.transaction.TransactionResponse;
import com.example.common.domain.transaction.TransactionsByAccountRequest;
import com.example.common.domain.transaction.TransactionsByAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping()
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.createTransaction(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<TransactionsByAccountResponse> getTransactions(@PathVariable Long accountId) {
        TransactionsByAccountRequest request = new TransactionsByAccountRequest(accountId);
        TransactionsByAccountResponse response = transactionService.getTransactionsByAccountId(request);
        return ResponseEntity.ok(response);
    }
}
