package com.example.account.service;

import com.example.common.dto.account.AccountCreationRequest;
import com.example.common.dto.account.AccountCreationResponse;
import com.example.common.dto.account.AccountGetRequest;
import com.example.common.dto.account.AccountGetResponse;
import com.example.account.mappers.AccountMapper;
import com.example.common.dto.balance.BalanceCreateRequest;
import com.example.common.dto.balance.BalanceCreateResponse;
import com.example.common.dto.balance.BalanceGetRequest;
import com.example.common.dto.balance.BalanceGetResponse;
import com.example.common.model.Account;
import com.example.account.publisher.MessagePublisher;
import com.example.account.serviceclient.BalanceService;
import com.example.common.exception.exceptions.BusinessException;
import com.example.common.model.Balance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.example.common.exception.enums.ErrorCode.ACCOUNT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper mapper;
    private final MessagePublisher accountMessagePublisher;
    private final BalanceService balanceService;

    @Transactional
    public AccountCreationResponse createAccount(AccountCreationRequest request) {

        Account account = new Account();
        account.setCustomerId(request.getCustomerId());
        account.setCountry(request.getCountry());

        List<Balance> balances = new ArrayList<>();
        request.getCurrencies().forEach(currency -> {
            Balance balance = new Balance();
            balance.setAccountId(account.getId());
            balance.setCurrency(currency);
            balance.setAvailableAmount(BigDecimal.ZERO);

            BalanceCreateRequest balanceCreateRequest = new BalanceCreateRequest(balance);
            BalanceCreateResponse response = balanceService.createAccountBalance(balanceCreateRequest);

            balances.addAll(response.getBalances());
        });


        mapper.insertAccount(account);


        // Publish an event to RabbitMQ
        accountMessagePublisher.publishAccountCreated(account);

        return new AccountCreationResponse(account.getId(), account.getCustomerId(), balances);
    }


    @Transactional(readOnly = true)
    public AccountGetResponse getAccount(AccountGetRequest request) {
        Long accountId = request.getAccountId();
        Account account = mapper.findAccountById(accountId);
        if (account == null) {
            throw new BusinessException(ACCOUNT_NOT_FOUND, "Account with ID " + accountId + " not found.");
        }

        BalanceGetRequest balanceGetRequest = new BalanceGetRequest(accountId);
        BalanceGetResponse response = balanceService.getAccountBalances(balanceGetRequest);
        List<Balance> balances = response.getBalances();
        return new AccountGetResponse(account.getId(), account.getCustomerId(), balances);
    }
}
