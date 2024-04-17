package com.example.common.gateway.account;

import com.example.common.domain.account.AccountCreationRequest;
import com.example.common.domain.account.AccountCreationResponse;
import com.example.common.domain.account.AccountGetRequest;
import com.example.common.domain.account.AccountGetResponse;
import com.example.common.exception.exceptions.BusinessException;
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
        AccountCreationResponse response = accountService.createAccount(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<AccountGetResponse> getAccount(@RequestBody AccountGetRequest request) {
        AccountGetResponse response = accountService.getAccount(request);
        return ResponseEntity.ok(response);
    }
}
