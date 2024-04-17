package com.example.common.domain.account;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCreationRequest implements Serializable {
    private Long customerId;
    private String country;
    private List<String> currencies;
}