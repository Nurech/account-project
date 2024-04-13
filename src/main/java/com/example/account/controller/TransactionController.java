package com.example.account.controller;

import com.example.account.dto.TransactionRequest;
import com.example.account.dto.TransactionResponse;
import com.example.account.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable Long accountId) {
        List<TransactionResponse> transactions = transactionService.getTransactions(accountId);
        return ResponseEntity.ok(transactions);
    }
}
