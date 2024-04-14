package com.example.transaction.config;

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
    Queue transactionCreateQueue() {
        return new Queue("create.transaction.queue", true);
    }

    @Bean
    Queue transactionByAccountIdQueue() {
        return new Queue("get.transactions.by.account.id.queue", true);
    }

    @Bean
    TopicExchange transactionExchange() {
        return new TopicExchange("transaction.exchange");
    }

    @Bean
    Binding bindingTransactionCreate(Queue transactionCreateQueue, TopicExchange transactionExchange) {
        return BindingBuilder.bind(transactionCreateQueue).to(transactionExchange).with("transaction.create.#");
    }

    @Bean
    Binding bindingTransactionByAccountId(Queue transactionByAccountIdQueue, TopicExchange transactionExchange) {
        return BindingBuilder.bind(transactionByAccountIdQueue).to(transactionExchange).with("get.transactions.by.account.id");
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
