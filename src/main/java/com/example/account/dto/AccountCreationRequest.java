package com.example.account.dto;

import lombok.Data;

import java.util.List;

@Data
public class AccountCreationRequest {
    private String customerId;
    private String country;
    private List<String> currencies;
}
