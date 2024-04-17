package com.example.common.gateway.account;

import com.example.common.domain.account.AccountCreationRequest;
import com.example.common.domain.account.AccountCreationResponse;
import com.example.common.domain.account.AccountGetRequest;
import com.example.common.domain.account.AccountGetResponse;
import com.example.common.dto.ResponseWrapperDTO;
import com.example.common.service.MessageProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final RabbitTemplate rabbitTemplate;
    private final MessageProcessingService messageProcessingService;

    public AccountCreationResponse createAccount(AccountCreationRequest request) {
        ParameterizedTypeReference<ResponseWrapperDTO<AccountCreationResponse>> responseType = new ParameterizedTypeReference<>() {};
        ResponseWrapperDTO<AccountCreationResponse> response = rabbitTemplate.convertSendAndReceiveAsType("accountExchange", "account.create", request, responseType);
        return messageProcessingService.processResponseWrapper(response);
    }


    public AccountGetResponse getAccount(AccountGetRequest request) {
        ParameterizedTypeReference<ResponseWrapperDTO<AccountGetResponse>> responseType = new ParameterizedTypeReference<>() {};
        ResponseWrapperDTO<AccountGetResponse> response = rabbitTemplate.convertSendAndReceiveAsType("accountExchange", "account.get", request, responseType);
        return messageProcessingService.processResponseWrapper(response);
    }



}