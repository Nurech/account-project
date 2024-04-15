package com.example.balance.service;

import com.example.balance.mappers.BalanceMapper;
import com.example.common.dto.balance.*;
import com.example.common.model.Balance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceMapper mapper;

    @Transactional
    public CreateBalanceResponse createBalance(CreateBalanceRequest request) {
        Balance balance = request.getBalance();
        mapper.insertBalance(balance);
        return new CreateBalanceResponse(balance.getAccountId(), List.of(balance));
    }


    public UpdateBalanceResponse updateBalance(UpdateBalanceRequest request) {
        mapper.updateBalance(request.getBalance());
        return new UpdateBalanceResponse(request.getBalance());

    }

    public GetAccountBalancesResponse getBalances(GetAccountBalancesRequest request) {
        List<Balance> balances = mapper.findBalancesByAccountId(request.getAccountId());
        return new GetAccountBalancesResponse(request.getAccountId(), balances);
    }

    public GetBalanceByCurrencyResponse getBalancesByCurrency(GetBalanceByCurrencyRequest request) {
        Balance balance = mapper.findBalanceByAccountIdAndCurrency(request.getAccountId(), request.getCurrency());
        if (balance == null) {
            log.warn("No balance found for Account ID: {}, Currency: {}", request.getAccountId(), request.getCurrency());
        }
        return new GetBalanceByCurrencyResponse(request.getAccountId(), balance);
    }

}
