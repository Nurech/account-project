package com.example.account.controller;

import com.example.account.dto.AccountCreationRequest;
import com.example.account.dto.AccountCreationResponse;
import com.example.account.dto.AccountGetRequest;
import com.example.account.dto.AccountGetResponse;
import com.example.account.model.Account;
import com.example.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountCreationResponse> createAccount(@RequestBody AccountCreationRequest request) {
        try {
            AccountCreationResponse response = accountService.createAccount(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<AccountGetResponse> getAccount(@RequestBody AccountGetRequest request) {
        try {
            AccountGetResponse response = accountService.getAccount(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
