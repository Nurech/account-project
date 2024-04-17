package com.example.account.listener;

import com.example.common.domain.account.AccountCreationRequest;
import com.example.common.domain.account.AccountCreationResponse;
import com.example.common.domain.account.AccountGetRequest;
import com.example.common.domain.account.AccountGetResponse;
import com.example.account.service.AccountService;
import com.example.common.dto.ErrorDTO;
import com.example.common.dto.ResponseWrapperDTO;
import com.example.common.exception.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountMessageListener {

    private final AccountService accountService;

    @RabbitListener(queues = "accountCreateQueue", returnExceptions = "true")
    public ResponseWrapperDTO<AccountCreationResponse> createAccountMessage(AccountCreationRequest request) {
        log.info("Received message: {}", request);
        try {
            AccountCreationResponse response = accountService.createAccount(request);
            return new ResponseWrapperDTO<>(response);
        } catch (BusinessException e) {
            ErrorDTO error = new ErrorDTO();
            error.setErrorCode(e.getCode());
            error.setErrorMessage(e.getMessage());
            return new ResponseWrapperDTO<>(error);
        }
    }

    @RabbitListener(queues = "accountGetQueue", returnExceptions = "true")
    public ResponseWrapperDTO<AccountGetResponse> getAccountMessage(AccountGetRequest request) {
        log.info("Received message: {}", request);
        try {
            AccountGetResponse response = accountService.getAccount(request);
            return new ResponseWrapperDTO<>(response);
        } catch (BusinessException e) {
            ErrorDTO error = new ErrorDTO();
            error.setErrorCode(e.getCode());
            error.setErrorMessage(e.getMessage());
            return new ResponseWrapperDTO<>(error);
        }
    }
}
