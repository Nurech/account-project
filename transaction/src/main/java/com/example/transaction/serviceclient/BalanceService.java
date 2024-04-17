package com.example.transaction.serviceclient;

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
public class BalanceService {

    private final RabbitTemplate rabbitTemplate;
    private final MessageProcessingService messageProcessingService;

    public void updateBalance(Balance balance) {
        UpdateBalanceRequest request = new UpdateBalanceRequest(balance);
        ParameterizedTypeReference<ResponseWrapperDTO<UpdateBalanceResponse>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseWrapperDTO<UpdateBalanceResponse> response = rabbitTemplate.convertSendAndReceiveAsType(
                "balance.exchange", "update.balance", request, responseType);
        messageProcessingService.processResponseWrapper(response);
    }

    public Balance getAccountBalanceByCurrency(Long accountId, String currency) {
        GetBalanceByCurrencyRequest request = new GetBalanceByCurrencyRequest(accountId, currency);
        ParameterizedTypeReference<ResponseWrapperDTO<GetBalanceByCurrencyResponse>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseWrapperDTO<GetBalanceByCurrencyResponse> response = rabbitTemplate.convertSendAndReceiveAsType(
                "balance.exchange", "get.balance.by.currency", request, responseType);
        return messageProcessingService.processResponseWrapper(response).getBalance();
    }
}
