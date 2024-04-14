package com.example.account.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    Queue balanceCreateQueue() {
        return new Queue("balanceCreateQueue", true, false, false);
    }

    @Bean
    Queue balanceUpdateQueue() {
        return new Queue("balanceUpdateQueue", true, false, false);
    }

    @Bean
    Queue balanceGetQueue() {
        return new Queue("balanceGetQueue", true, false, false);
    }

    @Bean
    TopicExchange balanceExchange() {
        return new TopicExchange("balanceExchange");
    }

    @Bean
    Binding bindingCreate(Queue balanceCreateQueue, TopicExchange balanceExchange) {
        return BindingBuilder.bind(balanceCreateQueue).to(balanceExchange).with("balance.create");
    }

    @Bean
    Binding bindingUpdate(Queue balanceUpdateQueue, TopicExchange balanceExchange) {
        return BindingBuilder.bind(balanceUpdateQueue).to(balanceExchange).with("balance.update");
    }

    @Bean
    Binding bindingGet(Queue balanceGetQueue, TopicExchange balanceExchange) {
        return BindingBuilder.bind(balanceGetQueue).to(balanceExchange).with("balance.get");
    }
}
