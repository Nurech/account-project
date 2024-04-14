package com.example.account.serviceclient;

import com.example.account.mappers.CurrencyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyMapper currencyMapper;

    @Cacheable(value = "currency", key = "#currency", unless = "#result == null")
    public boolean isCurrencyAllowed(String currency) {
        List<String> allowedCurrencies = currencyMapper.findAllowedCurrencies();
        return allowedCurrencies.contains(currency);
    }
}
