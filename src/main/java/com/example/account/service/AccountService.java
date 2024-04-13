package com.example.account.service;

import com.example.account.dto.AccountCreationRequest;
import com.example.account.dto.AccountCreationResponse;
import com.example.account.dto.AccountGetRequest;
import com.example.account.dto.AccountGetResponse;
import com.example.account.exception.exceptions.BusinessException;
import com.example.account.exception.exceptions.ErrorCode;
import com.example.account.model.Account;
import com.example.account.model.Balance;
import com.example.account.mappers.AccountMapper;
import com.example.account.publisher.AccountMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.example.account.exception.exceptions.ErrorCode.INVALID_CURRENCY;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final AccountMessagePublisher accountMessagePublisher;

    @Transactional
    public AccountCreationResponse createAccount(AccountCreationRequest request) {

        // Allow account creation only for valid currencies
        validateCurrencies(request.getCurrencies());

        Account account = new Account();
        account.setCustomerId(request.getCustomerId());
        account.setCountry(request.getCountry());
        accountMapper.insertAccount(account);

        List<Balance> balances = new ArrayList<>();
        for (String currency : request.getCurrencies()) {
            Balance balance = new Balance();
            balance.setAccountId(account.getId());
            balance.setCurrency(currency);
            balance.setAvailableAmount(BigDecimal.ZERO);
            accountMapper.insertBalance(balance);
            balances.add(balance);
        }

        // Publish an event to RabbitMQ
        accountMessagePublisher.publishAccountCreated(account);

        return new AccountCreationResponse(account.getId(), account.getCustomerId(), balances);
    }

    private void validateCurrencies(List<String> currencies) {
        List<String> allowedCurrencies = accountMapper.findAllowedCurrencies();
        for (String currency : currencies) {
            if (!allowedCurrencies.contains(currency)) {
                throw new BusinessException(INVALID_CURRENCY, currency);
            }
        }
    }


    @Transactional(readOnly = true)
    public AccountGetResponse getAccount(AccountGetRequest request) {
        Long accountId = request.getAccountId();
        Account account = accountMapper.findAccountById(accountId);
        if (account == null) {
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, "Account with ID " + accountId + " not found.");
        }

        List<Balance> balances = accountMapper.findBalancesByAccountId(accountId);
        return new AccountGetResponse(account.getId(), account.getCustomerId(), balances);
    }
}
