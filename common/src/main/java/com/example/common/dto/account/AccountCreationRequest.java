package com.example.common.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreationRequest implements Serializable {
    private Long customerId;
    private String country;
    private List<String> currencies;
}