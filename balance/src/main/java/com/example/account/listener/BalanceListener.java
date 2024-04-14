package com.example.account.listener;

import com.example.account.service.BalanceService;
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

    @RabbitListener(queues = "balanceCreateQueue")
    public BalanceCreateResponse createBalanceMessage(BalanceCreateRequest request) {
        log.info("Received message: {}", request);
        return service.createBalance(request);
    }

    @RabbitListener(queues = "balanceUpdateQueue")
    public BalanceUpdateResponse updateBalanceMessage(BalanceUpdateRequest request) {
        log.info("Received message: {}", request);
        return service.updateBalance(request);
    }

    @RabbitListener(queues = "balanceGetQueue")
    public BalanceGetResponse getBalancesMessage(BalanceGetRequest request) {
        log.info("Received message: {}", request);
        return service.getBalances(request);
    }
}
