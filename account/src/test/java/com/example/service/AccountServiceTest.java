package com.example.service;

import com.example.AccountApplicationBaseTest;
import com.example.account.listener.AccountMessageListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest extends AccountApplicationBaseTest {

    @Autowired
    private AccountMessageListener listener;

    @Test
    public void test() {
       assertNotNull(listener);
    }
}
