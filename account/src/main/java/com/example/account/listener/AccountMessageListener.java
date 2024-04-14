package com.example.account.listener;

import com.example.common.dto.account.AccountCreationRequest;
import com.example.common.dto.account.AccountCreationResponse;
import com.example.common.dto.account.AccountGetRequest;
import com.example.common.dto.account.AccountGetResponse;
import com.example.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountMessageListener {

    private final AccountService accountService;

    @RabbitListener(queues = "accountCreateQueue")
    public AccountCreationResponse createAccountMessage(AccountCreationRequest request) {
        log.info("Received message: {}", request);
        return accountService.createAccount(request);
    }

    @RabbitListener(queues = "accountGetQueue")
    public AccountGetResponse getAccountMessage(AccountGetRequest request) {
        log.info("Received message: {}", request);
        return accountService.getAccount(request);
    }
}
