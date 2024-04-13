package com.example.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreationRequest {
    private String customerId;
    private String country;
    private List<String> currencies;
}