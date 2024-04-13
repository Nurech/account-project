package com.example.account.controller;

import com.example.account.dto.AccountCreationRequest;
import com.example.account.dto.AccountResponse;
import com.example.account.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountCreationRequest request) throws JsonProcessingException {
        AccountResponse accountResponse = accountService.createAccount(request);
        return ResponseEntity.ok(accountResponse);
    }

}