package com.example.balance.listener;

import com.example.balance.service.BalanceService;
import com.example.common.dto.balance.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BalanceListener {

    private final BalanceService service;

    @RabbitListener(queues = "create.balance.queue")
    public CreateBalanceResponse createBalanceMessage(CreateBalanceRequest request) {
        log.info("Received message: {}", request);
        return service.createBalance(request);
    }

    @RabbitListener(queues = "update.balance.queue")
    public UpdateBalanceResponse updateBalanceMessage(UpdateBalanceRequest request) {
        log.info("Received message: {}", request);
        return service.updateBalance(request);
    }

    @RabbitListener(queues = "get.balances.queue")
    public GetAccountBalancesResponse getBalancesMessage(GetAccountBalancesRequest request) {
        log.info("Received message: {}", request);
        return service.getBalances(request);
    }
    @RabbitListener(queues = "get.balance.by.currency.queue")
    public GetBalanceByCurrencyResponse getBalanceByCurrencyMessage(GetBalanceByCurrencyRequest request) {
        log.info("Received message: {}", request);
        return service.getBalancesByCurrency(request);
    }

}
