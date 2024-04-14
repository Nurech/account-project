package com.example.account.service;

import com.example.account.mappers.BalanceMapper;
import com.example.common.model.Balance;
import com.example.common.dto.balance.*;
import com.example.common.exception.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.example.common.exception.enums.ErrorCode.INVALID_CURRENCY;
import static com.example.common.exception.enums.ErrorCode.NEGATIVE_AMOUNT;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceMapper mapper;
    private final CurrencyService currencyService;

    @Transactional
    public BalanceCreateResponse createBalance(BalanceCreateRequest request) {
        runValidations(request.getBalance());
        mapper.insertBalance(request.getBalance());
        return new BalanceCreateResponse();
    }


    public BalanceUpdateResponse updateBalance(BalanceUpdateRequest request) {
        runValidations(request.getBalance());
        Balance updatedBalance = mapper.updateBalance(request.getBalance());
        return new BalanceUpdateResponse(updatedBalance);

    }

    public BalanceGetResponse getBalances(BalanceGetRequest request) {
        List<Balance> balances = mapper.findBalancesByAccountId(request.getAccountId());
        return new BalanceGetResponse(request.getAccountId(), balances);
    }

    private void runValidations(Balance balance) {
        boolean isCurrencyAllowed = currencyService.isCurrencyAllowed(balance.getCurrency());
        if (!isCurrencyAllowed) {
            throw new BusinessException(INVALID_CURRENCY, balance.getCurrency());
        }

        boolean isAmountPositive = isAmountPositive(balance.getAvailableAmount());
        if (!isAmountPositive) {
            throw new BusinessException(NEGATIVE_AMOUNT, balance.getAvailableAmount());
        }
    }

    private boolean isAmountPositive(BigDecimal availableAmount) {
        if (availableAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(NEGATIVE_AMOUNT, availableAmount);
        }
        return true;
    }


}
