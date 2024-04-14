package com.example.account.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    Queue balanceCreateQueue() {
        return new Queue("create.balance.queue", true);
    }

    @Bean
    Queue balanceUpdateQueue() {
        return new Queue("update.balance.queue", true);
    }

    @Bean
    Queue balanceGetQueue() {
        return new Queue("get.balances.queue", true);
    }
    @Bean
    Queue getBalanceByCurrencyQueue() {
        return new Queue("get.balance.by.currency.queue", true);
    }

    @Bean
    TopicExchange balanceExchange() {
        return new TopicExchange("balance.exchange");
    }

    @Bean
    Binding bindingCreate(Queue balanceCreateQueue, TopicExchange balanceExchange) {
        return BindingBuilder.bind(balanceCreateQueue).to(balanceExchange).with("create.balance");
    }

    @Bean
    Binding bindingUpdate(Queue balanceUpdateQueue, TopicExchange balanceExchange) {
        return BindingBuilder.bind(balanceUpdateQueue).to(balanceExchange).with("update.balance");
    }

    @Bean
    Binding bindingGetByCurrency(Queue getBalanceByCurrencyQueue, TopicExchange balanceExchange) {
        return BindingBuilder.bind(getBalanceByCurrencyQueue).to(balanceExchange).with("get.balance.by.currency");
    }

    @Bean
    Binding bindingGetAccountBalances(Queue balanceGetQueue, TopicExchange balanceExchange) {
        return BindingBuilder.bind(balanceGetQueue).to(balanceExchange).with("get.balances");
    }

    @Bean
    public Jackson2JsonMessageConverter consumerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(consumerJackson2MessageConverter());
        factory.setAdviceChain(RetryInterceptorBuilder.stateless()
                .maxAttempts(1)
                .build());
        return factory;
    }
}
