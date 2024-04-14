package com.example.common.gateway.transaction;

import com.example.common.dto.transaction.TransactionRequest;
import com.example.common.dto.transaction.TransactionResponse;
import com.example.common.dto.transaction.TransactionsByAccountRequest;
import com.example.common.dto.transaction.TransactionsByAccountResponse;
import com.example.common.exception.exceptions.NoResponseFromRabbitException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final RabbitTemplate rabbitTemplate;

    //createTransaction
    public TransactionResponse createTransaction(TransactionRequest request) {
        TransactionResponse response = (TransactionResponse) rabbitTemplate.convertSendAndReceive("transactionExchange", "transactionCreate", request);
        if (response == null) {
            throw new NoResponseFromRabbitException(request);
        }
        return response;
    }

    public TransactionsByAccountResponse getTransactionsByAccountId(TransactionsByAccountRequest request) {
        TransactionsByAccountResponse response = (TransactionsByAccountResponse) rabbitTemplate.convertSendAndReceive("transactionExchange", "transactionByAccountId", request);
        if (response == null) {
            throw new NoResponseFromRabbitException(request);
        }
        return response;
    }

}
