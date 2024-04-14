package com.example.common.gateway.account;

import com.example.common.dto.account.AccountCreationRequest;
import com.example.common.dto.account.AccountCreationResponse;
import com.example.common.dto.account.AccountGetRequest;
import com.example.common.dto.account.AccountGetResponse;
import com.example.common.exception.exceptions.NoResponseFromRabbitException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final RabbitTemplate rabbitTemplate;

    public AccountCreationResponse createAccount(AccountCreationRequest request) {
        AccountCreationResponse response = (AccountCreationResponse) rabbitTemplate.convertSendAndReceive("accountExchange", "account.create", request);
        if (response == null) {
            throw new NoResponseFromRabbitException(request);
        }
        return response;
    }

    public AccountGetResponse getAccount(AccountGetRequest request) {
        AccountGetResponse response = (AccountGetResponse) rabbitTemplate.convertSendAndReceive("accountExchange", "account.get", request);
        if (response == null) {
            throw new NoResponseFromRabbitException(request);
        }
        return response;
    }

}
