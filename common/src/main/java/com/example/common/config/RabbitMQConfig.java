package com.example.common.config;

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
        return new TopicExchange("transaction.exchange");
    }

    @Bean
    public TopicExchange balanceExchange() {
        return new TopicExchange("balance.exchange");
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
