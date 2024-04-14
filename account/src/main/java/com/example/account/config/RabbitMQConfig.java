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
    Queue createAccountQueue() {
        return new Queue("accountCreateQueue", true);
    }

    @Bean
    Queue getAccountQueue() {
        return new Queue("accountGetQueue", true);
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
