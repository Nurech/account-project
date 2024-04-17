package com.example.account.serviceclient;

import com.example.account.service.MessageProcessingService;
import com.example.common.domain.balance.CreateBalanceRequest;
import com.example.common.domain.balance.CreateBalanceResponse;
import com.example.common.domain.balance.GetAccountBalancesRequest;
import com.example.common.domain.balance.GetAccountBalancesResponse;
import com.example.common.dto.ResponseWrapperDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BalanceService {

    private final RabbitTemplate rabbitTemplate;
    private final MessageProcessingService messageProcessingService;


    public CreateBalanceResponse createAccountBalance(CreateBalanceRequest request) {
        ParameterizedTypeReference<ResponseWrapperDTO<CreateBalanceResponse>> responseType = new ParameterizedTypeReference<>() {};
        ResponseWrapperDTO<CreateBalanceResponse> response = rabbitTemplate.convertSendAndReceiveAsType(
                "balance.exchange", "create.balance", request, responseType
        );
        return messageProcessingService.processResponseWrapper(response);
    }

    public GetAccountBalancesResponse getAccountBalances(GetAccountBalancesRequest request) {
        ParameterizedTypeReference<ResponseWrapperDTO<GetAccountBalancesResponse>> response = new ParameterizedTypeReference<>() {};
        ResponseWrapperDTO<GetAccountBalancesResponse> responseWrapperDTO = rabbitTemplate.convertSendAndReceiveAsType(
                "balance.exchange", "get.balances", request, response
        );
        return messageProcessingService.processResponseWrapper(responseWrapperDTO);
    }
}
