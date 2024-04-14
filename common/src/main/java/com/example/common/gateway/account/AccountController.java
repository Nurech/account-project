package com.example.common.gateway.account;

import com.example.common.dto.account.AccountCreationRequest;
import com.example.common.dto.account.AccountCreationResponse;
import com.example.common.dto.account.AccountGetRequest;
import com.example.common.dto.account.AccountGetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountCreationResponse> createAccount(@RequestBody AccountCreationRequest request) {
        try {
            AccountCreationResponse response = accountService.createAccount(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Failed to create account", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<AccountGetResponse> getAccount(@RequestBody AccountGetRequest request) {
        try {
            AccountGetResponse response = accountService.getAccount(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Failed to get account", e);
            return ResponseEntity.badRequest().body(null);
        }
    }
}
