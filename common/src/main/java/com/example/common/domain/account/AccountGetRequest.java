package com.example.common.domain.account;

import com.example.common.dto.ErrorDTO;
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