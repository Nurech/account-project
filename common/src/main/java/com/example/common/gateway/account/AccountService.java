package com.example.common.gateway.account;

import com.example.common.dto.account.AccountCreationRequest;
import com.example.common.dto.account.AccountCreationResponse;
import com.example.common.dto.account.AccountGetRequest;
import com.example.common.dto.account.AccountGetResponse;
import com.example.common.exception.exceptions.BusinessException;
import com.example.common.exception.exceptions.NoResponseFromRabbitException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.example.common.exception.enums.ErrorCode.ACCOUNT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final RabbitTemplate rabbitTemplate;

    private final MessagePostProcessor jsonMessagePostProcessor = message -> {
        message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
        return message;
    };

    public AccountCreationResponse createAccount(AccountCreationRequest request) {
        AccountCreationResponse response = (AccountCreationResponse) rabbitTemplate.convertSendAndReceive(
                "accountExchange", "account.create", request, jsonMessagePostProcessor);
        if (response == null) {
            throw new NoResponseFromRabbitException(request);
        }
        return response;
    }

    public AccountGetResponse getAccount(AccountGetRequest request) {
        AccountGetResponse response = (AccountGetResponse) rabbitTemplate.convertSendAndReceive(
                "accountExchange", "account.get", request, jsonMessagePostProcessor);
        if (response == null) {
            throw new BusinessException(ACCOUNT_NOT_FOUND);
        }
        return response;
    }
}