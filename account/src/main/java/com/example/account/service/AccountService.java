package com.example.account.service;

import com.example.account.serviceclient.CurrencyService;
import com.example.common.dto.account.AccountCreationRequest;
import com.example.common.dto.account.AccountCreationResponse;
import com.example.common.dto.account.AccountGetRequest;
import com.example.common.dto.account.AccountGetResponse;
import com.example.account.mappers.AccountMapper;
import com.example.common.dto.balance.CreateBalanceRequest;
import com.example.common.dto.balance.CreateBalanceResponse;
import com.example.common.dto.balance.GetAccountBalancesRequest;
import com.example.common.dto.balance.GetAccountBalancesResponse;
import com.example.common.model.Account;
import com.example.account.publisher.MessagePublisher;
import com.example.account.serviceclient.BalanceService;
import com.example.common.exception.exceptions.BusinessException;
import com.example.common.model.Balance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.example.common.exception.enums.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper mapper;
    private final MessagePublisher accountMessagePublisher;
    private final BalanceService balanceService;
    private final CurrencyService currencyService;

    public AccountCreationResponse createAccount(AccountCreationRequest request) {
        Account account = new Account();
        account.setCustomerId(request.getCustomerId());
        account.setCountry(request.getCountry());

        try {

            for (String currency : request.getCurrencies()) {
                if (!currencyService.isCurrencyAllowed(currency)) {
                    throw new BusinessException(INVALID_CURRENCY, "Currency " + currency + " is not allowed.");
                }
            }
            mapper.insertAccount(account);

            List<Balance> balances = new ArrayList<>();

            // Create balances
            for (String currency : request.getCurrencies()) {
                Balance balance = new Balance();
                balance.setAccountId(account.getId());
                balance.setCurrency(currency);
                balance.setAvailableAmount(BigDecimal.ZERO);

                CreateBalanceRequest balanceCreateRequest = new CreateBalanceRequest(balance);
                CreateBalanceResponse response = balanceService.createAccountBalance(balanceCreateRequest);
                balances.addAll(response.getBalances());
            }

            accountMessagePublisher.publishAccountCreated(account);
            return new AccountCreationResponse(account.getId(), account.getCustomerId(), balances);

        } catch (BusinessException e) {
            throw e; // rethrow the exception to be handled by the controller
        }

    }


    @Transactional(readOnly = true)
    public AccountGetResponse getAccount(AccountGetRequest request) {
        Long accountId = request.getAccountId();
        Account account = mapper.findAccountById(accountId);
        if (account == null) {
            log.warn("Account not found for ID: {}", accountId);
            throw new BusinessException(ACCOUNT_NOT_FOUND, "Account with ID " + accountId + " not found.");
        }

        GetAccountBalancesRequest balanceGetRequest = new GetAccountBalancesRequest(accountId);
        GetAccountBalancesResponse response = balanceService.getAccountBalances(balanceGetRequest);
        List<Balance> balances = response.getBalances();

        return new AccountGetResponse(account.getId(), account.getCustomerId(), balances);
    }
}