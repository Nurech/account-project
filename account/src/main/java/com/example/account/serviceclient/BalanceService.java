package com.example.account.serviceclient;

import com.example.common.dto.balance.CreateBalanceRequest;
import com.example.common.dto.balance.CreateBalanceResponse;
import com.example.common.dto.balance.GetAccountBalancesRequest;
import com.example.common.dto.balance.GetAccountBalancesResponse;
import com.example.common.exception.exceptions.NoResponseFromRabbitException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BalanceService {

    private final RabbitTemplate rabbitTemplate;


    public CreateBalanceResponse createAccountBalance(CreateBalanceRequest request) {
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
            return message;
        };
        CreateBalanceResponse response = (CreateBalanceResponse) rabbitTemplate.convertSendAndReceive(
                "balance.exchange", "create.balance", request, messagePostProcessor
        );
        if (response == null) {
            throw new NoResponseFromRabbitException(request);
        }
        return response;
    }

    public GetAccountBalancesResponse getAccountBalances(GetAccountBalancesRequest request) {
        GetAccountBalancesResponse response = (GetAccountBalancesResponse) rabbitTemplate.convertSendAndReceive(
                "balance.exchange", "get.balances", request, message -> {
                    message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
                    return message;
                });
        if (response == null) {
            throw new NoResponseFromRabbitException(request);
        }
        return response;
    }
}
