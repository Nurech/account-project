package com.example.transaction.serviceclient;

import com.example.common.domain.account.AccountGetRequest;
import com.example.common.domain.account.AccountGetResponse;
import com.example.common.domain.balance.GetBalanceByCurrencyRequest;
import com.example.common.domain.balance.GetBalanceByCurrencyResponse;
import com.example.common.domain.balance.UpdateBalanceRequest;
import com.example.common.domain.balance.UpdateBalanceResponse;
import com.example.common.dto.ResponseWrapperDTO;
import com.example.common.model.Balance;
import com.example.transaction.service.MessageProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final RabbitTemplate rabbitTemplate;
    private final MessageProcessingService messageProcessingService;

    public boolean checkAccountExists(Long accountId) {
        AccountGetRequest request = new AccountGetRequest(accountId);
        ParameterizedTypeReference<ResponseWrapperDTO<AccountGetResponse>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseWrapperDTO<AccountGetResponse> response = rabbitTemplate.convertSendAndReceiveAsType(
                "accountExchange", "account.get", request, responseType);
        return messageProcessingService.processResponseWrapper(response) != null;
    }
}
