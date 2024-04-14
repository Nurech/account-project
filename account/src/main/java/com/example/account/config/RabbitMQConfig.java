package com.example.account.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    Queue createAccountQueue() {
        return new Queue("accountCreateQueue", true, false, false);
    }

    @Bean
    Queue getAccountQueue() {
        return new Queue("accountGetQueue", true, false, false);
    }

    @Bean
    TopicExchange accountExchange() {
        return new TopicExchange("accountExchange");
    }

    @Bean
    Binding bindingCreate(Queue createAccountQueue, TopicExchange accountExchange) {
        return BindingBuilder.bind(createAccountQueue).to(accountExchange).with("account.create");
    }

    @Bean
    Binding bindingGet(Queue getAccountQueue, TopicExchange accountExchange) {
        return BindingBuilder.bind(getAccountQueue).to(accountExchange).with("account.get");
    }
}
