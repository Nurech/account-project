package com.example.common.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue accountQueue() {
        return new Queue("accountQueue", true);
    }

    @Bean
    public Queue transactionQueue() {
        return new Queue("transactionQueue", true);
    }

    @Bean
    public Queue balanceQueue() {
        return new Queue("balanceQueue", true);
    }

    @Bean
    public TopicExchange accountExchange() {
        return new TopicExchange("accountExchange");
    }

    @Bean
    public TopicExchange transactionExchange() {
        return new TopicExchange("transactionExchange");
    }

    @Bean
    public TopicExchange balanceExchange() {
        return new TopicExchange("balanceExchange");
    }

    @Bean
    public Binding accountBinding(Queue accountQueue, TopicExchange accountExchange) {
        return BindingBuilder.bind(accountQueue).to(accountExchange).with("account.#");
    }

    @Bean
    public Binding transactionBinding(Queue transactionQueue, TopicExchange transactionExchange) {
        return BindingBuilder.bind(transactionQueue).to(transactionExchange).with("transaction.#");
    }

    @Bean
    public Binding balanceBinding(Queue balanceQueue, TopicExchange balanceExchange) {
        return BindingBuilder.bind(balanceQueue).to(balanceExchange).with("balance.#");
    }
}
