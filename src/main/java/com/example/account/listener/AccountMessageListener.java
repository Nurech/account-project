package com.example.account.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class AccountMessageListener {

    @RabbitListener(queues = "accountQueue")
    public void receiveAccountMessage(Object accountMessage) {
        System.out.println("Received message: " + accountMessage);
        // Process the message as needed
    }
}
