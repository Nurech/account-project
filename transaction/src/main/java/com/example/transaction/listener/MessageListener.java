package com.example.transaction.listener;

import com.example.common.domain.transaction.TransactionRequest;
import com.example.common.domain.transaction.TransactionResponse;
import com.example.common.domain.transaction.TransactionsByAccountRequest;
import com.example.common.domain.transaction.TransactionsByAccountResponse;
import com.example.common.dto.ErrorDTO;
import com.example.common.dto.ResponseWrapperDTO;
import com.example.common.exception.exceptions.BusinessException;
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
    public ResponseWrapperDTO<TransactionResponse> createTransactionMessage(TransactionRequest request) {
        log.info("Received message for creating transaction: {}", request);
        try {
            TransactionResponse response = service.createTransaction(request);
            return new ResponseWrapperDTO<>(response);
        } catch (BusinessException e) {
            ErrorDTO error = new ErrorDTO();
            error.setErrorCode(e.getCode());
            error.setErrorMessage(e.getMessage());
            return new ResponseWrapperDTO<>(error);
        }
    }

    @RabbitListener(queues = "get.transactions.by.account.id.queue")
    public ResponseWrapperDTO<TransactionsByAccountResponse> getTransactionsByAccountIdMessage(TransactionsByAccountRequest request) {
        log.info("Received message for getting transactions by account ID: {}", request);
        try {
            TransactionsByAccountResponse response = service.getTransactions(request);
            return new ResponseWrapperDTO<>(response);
        } catch (BusinessException e) {
            ErrorDTO error = new ErrorDTO();
            error.setErrorCode(e.getCode());
            error.setErrorMessage(e.getMessage());
            return new ResponseWrapperDTO<>(error);
        }
    }
}
