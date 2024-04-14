package com.example.transaction.serviceclient;

import com.example.common.dto.balance.*;
import com.example.common.exception.exceptions.NoResponseFromRabbitException;
import com.example.common.model.Balance;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BalanceService {

    private final RabbitTemplate rabbitTemplate;

    public Balance updateBalance(Balance balance) {
        UpdateBalanceRequest request = new UpdateBalanceRequest(balance);
        UpdateBalanceResponse response = (UpdateBalanceResponse) rabbitTemplate.convertSendAndReceive(
                "balance.exchange", "update.balance", request, message -> {
                    message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
                    return message;
                });
        if (response == null) {
            throw new NoResponseFromRabbitException(request);
        }
        return response.getBalance();
    }

    public Balance getAccountBalanceByCurrency(Long accountId, String currency) {
        GetBalanceByCurrencyRequest request = new GetBalanceByCurrencyRequest(accountId, currency);
        GetBalanceByCurrencyResponse response = (GetBalanceByCurrencyResponse) rabbitTemplate.convertSendAndReceive(
                "balance.exchange", "get.balance.by.currency", request, message -> {
                    message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
                    return message;
                });
        if (response == null) {
            throw new NoResponseFromRabbitException(request);
        }
        return response.getBalance();
    }

}
