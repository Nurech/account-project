package com.example.common.gateway.transaction;

import com.example.common.dto.transaction.TransactionRequest;
import com.example.common.dto.transaction.TransactionResponse;
import com.example.common.dto.transaction.TransactionsByAccountRequest;
import com.example.common.dto.transaction.TransactionsByAccountResponse;
import com.example.common.exception.exceptions.NoResponseFromRabbitException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final RabbitTemplate rabbitTemplate;

    private final MessagePostProcessor jsonMessagePostProcessor = message -> {
        message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
        return message;
    };

    public TransactionResponse createTransaction(TransactionRequest request) {
        TransactionResponse response = (TransactionResponse) rabbitTemplate.convertSendAndReceive(
                "transaction.exchange", "transaction.create", request, jsonMessagePostProcessor);
        if (response == null) {
            throw new NoResponseFromRabbitException(request);
        }
        return response;
    }

    public TransactionsByAccountResponse getTransactionsByAccountId(TransactionsByAccountRequest request) {
        TransactionsByAccountResponse response = (TransactionsByAccountResponse) rabbitTemplate.convertSendAndReceive(
                "transaction.exchange", "get.transactions.by.account.id", request, jsonMessagePostProcessor);
        if (response == null) {
            throw new NoResponseFromRabbitException(request);
        }
        return response;
    }
}