package com.example.account.serviceclient;

import com.example.common.dto.account.AccountCreationRequest;
import com.example.common.dto.account.AccountCreationResponse;
import com.example.common.dto.balance.BalanceCreateRequest;
import com.example.common.dto.balance.BalanceCreateResponse;
import com.example.common.dto.balance.BalanceGetRequest;
import com.example.common.dto.balance.BalanceGetResponse;
import com.example.common.exception.exceptions.NoResponseFromRabbitException;
import com.example.common.model.Balance;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final RabbitTemplate rabbitTemplate;


    public BalanceCreateResponse createAccountBalance(BalanceCreateRequest request) {
        BalanceCreateResponse response = (BalanceCreateResponse) rabbitTemplate.convertSendAndReceive("balanceExchange", "balanceCreate", request);
        if (response == null) {
            throw new NoResponseFromRabbitException(request);
        }
        return response;
    }

    public BalanceGetResponse getAccountBalances(BalanceGetRequest request) {
        BalanceGetResponse response = (BalanceGetResponse) rabbitTemplate.convertSendAndReceive("balanceExchange", "balancesGet", request);
        if (response == null) {
            throw new NoResponseFromRabbitException(request);
        }
        return response;
    }
}
