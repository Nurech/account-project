package com.example.service;

import com.example.AccountApplicationBaseTest;
import com.example.account.listener.AccountMessageListener;
import com.example.account.mappers.AccountMapper;
import com.example.account.publisher.MessagePublisher;
import com.example.account.service.AccountService;
import com.example.common.domain.account.AccountCreationRequest;
import com.example.common.domain.account.AccountCreationResponse;
import com.example.common.domain.account.AccountGetRequest;
import com.example.common.domain.account.AccountGetResponse;
import com.example.common.exception.exceptions.BusinessException;
import com.example.common.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static com.example.common.exception.enums.ErrorCode.INVALID_CURRENCY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTest extends AccountApplicationBaseTest {

    // SpyBean It allows us to create a spy (partial mock) of a Spring bean
    // while still using the actual bean definition from our application context.
    @SpyBean
    private AccountMessageListener listener;

    @SpyBean
    private AccountService accountService;

    @SpyBean
    private AccountMapper accountMapper;

    @SpyBean
    private MessagePublisher messagePublisher;

    @Test
    public void testServices() {
        assertNotNull(listener);
        assertNotNull(accountService);
        assertNotNull(accountMapper);
        assertNotNull(messagePublisher);
    }

    @Test
    public void mustNotFindAccount() {
        AccountGetRequest request = AccountGetRequest.builder()
                .accountId(1L)
                .build();
        assertThrows(BusinessException.class, () -> accountService.getAccount(request),
                "Should not have found non existent account.");
    }

    @Test
    public void mustFindAccountSuccessfully() {
        // Create the account then find it from the database
        AccountCreationRequest request = AccountCreationRequest.builder()
                .customerId(5L)
                .country("US")
                .currencies(List.of("USD"))
                .build();
        accountService.createAccount(request);

        AccountGetRequest getRequest = AccountGetRequest.builder()
                .accountId(request.getCustomerId())
                .build();
        AccountGetResponse response = accountService.getAccount(getRequest);

        assertNotNull(response);
        assertEquals(response.getCustomerId(), request.getCustomerId());
        assertNotNull(response.getBalances());
    }

    @Test
    public void invalidCurrencyShouldThrowBusinessException() {
        AccountCreationRequest request = AccountCreationRequest.builder()
                .customerId(2L)
                .country("US")
                .currencies(List.of("USD", "XXX"))
                .build();

        BusinessException exception = assertThrows(BusinessException.class,
                () -> accountService.createAccount(request),
                "Expected createAccount to throw BusinessException when currency is invalid");

        assertEquals(INVALID_CURRENCY.getCode(), exception.getCode());

        // Verify no account was inserted into the database
        verify(accountMapper, never()).insertAccount(any(Account.class));

        // Check that no messages were published
        verify(messagePublisher, never()).publishAccountCreated(any(Account.class));
    }

    @Test
    public void validDataShouldCreateAccountAndBalances() {
        AccountCreationRequest request = AccountCreationRequest.builder()
                .customerId(3L)
                .country("US")
                .currencies(List.of("USD", "EUR"))
                .build();

        AccountCreationResponse response = accountService.createAccount(request);

        assertNotNull(response);
        assertEquals(3L, response.getCustomerId());
        assertNotNull(response.getAccountId());
        assertFalse(response.getBalances().isEmpty());
    }

    @Test
    public void accountCreationShouldPublishAccountCreatedEvent() {
        AccountCreationRequest request = AccountCreationRequest.builder()
                .customerId(4L)
                .country("US")
                .currencies(List.of("USD"))
                .build();

        AccountCreationResponse response = accountService.createAccount(request);

        assertNotNull(response);
        assertEquals(4L, response.getCustomerId());
        assertNotNull(response.getAccountId());
        assertFalse(response.getBalances().isEmpty());
        assertEquals(1, response.getBalances().size());

        // Verify that the publish method was called
        verify(messagePublisher).publishAccountCreated(any(Account.class));
    }

    @Test
    public void shouldThrowErrorWhenCustomerIdIsNull() {
        AccountCreationRequest request = AccountCreationRequest.builder()
                .customerId(null)
                .country("US")
                .currencies(List.of("USD"))
                .build();

        assertThrows(BusinessException.class,
                () -> accountService.createAccount(request),
                "Expected to throw BusinessException when customerId is null");
    }

}
