package com.example.balance.listener;

import com.example.balance.service.BalanceService;
import com.example.common.domain.balance.*;
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
public class BalanceListener {

    private final BalanceService service;

    @RabbitListener(queues = "create.balance.queue")
    public ResponseWrapperDTO<CreateBalanceResponse> createBalanceMessage(CreateBalanceRequest request) {
        log.info("Received message create balance: {}", request);
        try {
            CreateBalanceResponse response = service.createBalance(request);
            return new ResponseWrapperDTO<>(response);
        } catch (BusinessException e) {
            ErrorDTO error = new ErrorDTO();
            error.setErrorCode(e.getCode());
            error.setErrorMessage(e.getMessage());
            return new ResponseWrapperDTO<>(error);
        }
    }

    @RabbitListener(queues = "update.balance.queue")
    public ResponseWrapperDTO<UpdateBalanceResponse> updateBalanceMessage(UpdateBalanceRequest request) {
        log.info("Received message update balance: {}", request);
        try {
            UpdateBalanceResponse response = service.updateBalance(request);
            return new ResponseWrapperDTO<>(response);
        } catch (BusinessException e) {
            ErrorDTO error = new ErrorDTO();
            error.setErrorCode(e.getCode());
            error.setErrorMessage(e.getMessage());
            return new ResponseWrapperDTO<>(error);
        }
    }

    @RabbitListener(queues = "get.balances.queue")
    public ResponseWrapperDTO<GetAccountBalancesResponse> getBalancesMessage(GetAccountBalancesRequest request) {
        log.info("Received message get balances: {}", request);
        try {
            GetAccountBalancesResponse response = service.getBalances(request);
            return new ResponseWrapperDTO<>(response);
        } catch (BusinessException e) {
            ErrorDTO error = new ErrorDTO();
            error.setErrorCode(e.getCode());
            error.setErrorMessage(e.getMessage());
            return new ResponseWrapperDTO<>(error);
        }
    }

    @RabbitListener(queues = "get.balance.by.currency.queue")
    public ResponseWrapperDTO<GetBalanceByCurrencyResponse> getBalanceByCurrencyMessage(GetBalanceByCurrencyRequest request) {
        log.info("Received message get balance by currency: {}", request);
        try {
            GetBalanceByCurrencyResponse response = service.getBalancesByCurrency(request);
            return new ResponseWrapperDTO<>(response);
        } catch (BusinessException e) {
            ErrorDTO error = new ErrorDTO();
            error.setErrorCode(e.getCode());
            error.setErrorMessage(e.getMessage());
            return new ResponseWrapperDTO<>(error);
        }

    }

}
