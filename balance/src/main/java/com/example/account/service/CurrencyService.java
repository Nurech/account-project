package com.example.account.service;

import com.example.account.mappers.BalanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final BalanceMapper balanceMapper;

    @Cacheable(value = "currency", key = "#currency", unless = "#result == null")
    public boolean isCurrencyAllowed(String currency) {
        List<String> allowedCurrencies = balanceMapper.findAllowedCurrencies();
        return allowedCurrencies.contains(currency);
    }
}
