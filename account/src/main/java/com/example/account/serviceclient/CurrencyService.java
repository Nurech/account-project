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

    /**
     * Check if the currency is accepted by the system
     *
     * @param currency currency to check
     * @return true if currency is accepted, false otherwise
     */
    @Cacheable(value = "currency", key = "#currency", unless = "#result == null")
    // Use cache to store the allowed currencies as they are not expected to change frequently
    public boolean isCurrencyAllowed(String currency) {
        List<String> allowedCurrencies = currencyMapper.findAllowedCurrencies();
        return allowedCurrencies.contains(currency);
    }

}
