package com.example.transaction.listener;

import com.example.common.dto.transaction.TransactionRequest;
import com.example.common.dto.transaction.TransactionResponse;
import com.example.common.dto.transaction.TransactionsByAccountRequest;
import com.example.common.dto.transaction.TransactionsByAccountResponse;
import com.example.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageListener {

    private final TransactionService service;

    @RabbitListener(queues = "create.transaction.queue")
    public TransactionResponse createTransactionMessage(TransactionRequest request) {
        log.info("Received message: {}", request);
        return service.createTransaction(request);
    }

    @RabbitListener(queues = "get.transactions.by.account.id.queue")
    public TransactionsByAccountResponse getTransactionsByAccountIdMessage(TransactionsByAccountRequest request) {
        log.info("Received message: {}", request);
        return service.getTransactions(request);
    }
}
