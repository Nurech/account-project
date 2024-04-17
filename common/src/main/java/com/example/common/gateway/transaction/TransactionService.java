package com.example.common.gateway.transaction;

import com.example.common.dto.ResponseWrapperDTO;
import com.example.common.service.MessageProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import com.example.common.domain.transaction.TransactionRequest;
import com.example.common.domain.transaction.TransactionResponse;
import com.example.common.domain.transaction.TransactionsByAccountRequest;
import com.example.common.domain.transaction.TransactionsByAccountResponse;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final RabbitTemplate rabbitTemplate;
    private final MessageProcessingService messageProcessingService;

    public TransactionResponse createTransaction(TransactionRequest request) {
        ParameterizedTypeReference<ResponseWrapperDTO<TransactionResponse>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseWrapperDTO<TransactionResponse> response = rabbitTemplate.convertSendAndReceiveAsType(
                "transaction.exchange", "transaction.create", request, responseType);
        return messageProcessingService.processResponseWrapper(response);
    }

    public TransactionsByAccountResponse getTransactionsByAccountId(TransactionsByAccountRequest request) {
        ParameterizedTypeReference<ResponseWrapperDTO<TransactionsByAccountResponse>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseWrapperDTO<TransactionsByAccountResponse> response = rabbitTemplate.convertSendAndReceiveAsType(
                "transaction.exchange", "get.transactions.by.account.id", request, responseType);
        return messageProcessingService.processResponseWrapper(response);
    }
}
