package com.example.common.domain.account;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountGetRequest implements Serializable {
    private Long accountId;
}