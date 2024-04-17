package com.example.balance.service;

import com.example.balance.mappers.BalanceMapper;
import com.example.balance.serviceclient.CurrencyService;
import com.example.common.domain.balance.*;
import com.example.common.exception.exceptions.BusinessException;
import com.example.common.model.Balance;
import com.example.common.model.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.common.exception.enums.ErrorCode.BALANCE_MISSING;
import static com.example.common.exception.enums.ErrorCode.INVALID_CURRENCY;

@Service
@Slf4j
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceMapper mapper;
    private final CurrencyService currencyService;

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
        boolean isCurrencyAllowed = currencyService.isCurrencyAllowed(request.getCurrency());
        if (!isCurrencyAllowed) {
            log.warn("Currency {} is not allowed", request.getCurrency());
            throw new BusinessException(INVALID_CURRENCY);
        }
        Balance balance = mapper.findBalanceByAccountIdAndCurrency(request.getAccountId(), request.getCurrency());
        if (balance == null) {
            throw new BusinessException(BALANCE_MISSING);
        }
        return new GetBalanceByCurrencyResponse(request.getAccountId(), balance);
    }

}
