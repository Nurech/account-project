package com.example.transaction.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    Queue transactionCreateQueue() {
        return new Queue("transactionCreateQueue", true, false, false);
    }

    @Bean
    Queue transactionByAccountIdQueue() {
        return new Queue("transactionByAccountId", true, false, false);
    }

    @Bean
    TopicExchange transactionExchange() {
        return new TopicExchange("transactionExchange");
    }

    @Bean
    Binding bindingTransactionCreate(Queue transactionCreateQueue, TopicExchange transactionExchange) {
        return BindingBuilder.bind(transactionCreateQueue).to(transactionExchange).with("transaction.create.#");
    }

    @Bean
    Binding bindingTransactionByAccountId(Queue transactionByAccountIdQueue, TopicExchange transactionExchange) {
        return BindingBuilder.bind(transactionByAccountIdQueue).to(transactionExchange).with("transaction.byAccountId.#");
    }
}
