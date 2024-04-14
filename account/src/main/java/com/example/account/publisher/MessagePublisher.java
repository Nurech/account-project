package com.example.account.publisher;

import com.example.common.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessagePublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishAccountCreated(Account account) {
        rabbitTemplate.convertAndSend("accountExchange", "accountCreated", account);
    }
}
