package com.example.account.service;

import com.example.account.dto.AccountCreationRequest;
import com.example.account.dto.AccountResponse;
import com.example.account.model.Account;
import com.example.account.mappers.AccountMapper;
import com.example.account.publisher.AccountMessagePublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountMapper accountMapper;
    private final AccountMessagePublisher accountMessagePublisher;
    private final ObjectMapper objectMapper;

    @Transactional
    public AccountResponse createAccount(AccountCreationRequest request) throws JsonProcessingException {
        // Map the request DTO to the Account model
        Account account = new Account();
        account.setCustomerId(request.getCustomerId());
        account.setCountry(request.getCountry());

        // Insert the account into the database
        accountMapper.insertAccount(account);

        // TODO: Create balances and insert them too

        // Convert account to JSON before publishing
        String accountJson = objectMapper.writeValueAsString(account);
        accountMessagePublisher.publishAccountCreated(accountJson);

        // Convert to AccountResponse DTO
        AccountResponse response = new AccountResponse();
        response.setAccountId(account.getId());
        response.setCustomerId(account.getCustomerId());

        return response;
    }
}
